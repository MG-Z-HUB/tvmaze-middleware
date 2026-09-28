# TV Maze Middleware API

> Backend technical assessment — REST middleware for TV Maze with
> MongoDB caching and comments.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-green?logo=mongodb)](https://www.mongodb.com/atlas)
[![Maven](https://img.shields.io/badge/Maven-3.9%2B-C71A36?logo=apachemaven)](https://maven.apache.org/)

## Overview

**TV Maze Middleware API** is a REST middleware developed as part of a
Backend technical assessment.

The application consumes the public [TV Maze
API](https://www.tvmaze.com/api) and exposes its own REST endpoints,
adding:

- TV show search.
- TV show detail retrieval.
- MongoDB-based caching.
- Comments associated with shows.
- Ratings from **0 to 5**.
- Comments included in search and detail responses.
- Request validation.
- MongoDB Atlas support through an environment variable.
- Separation of responsibilities between controllers, services,
  repositories, and the external API client.

## Architecture

``` text
                         ┌──────────────────┐
                         │      Client      │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │   Controllers    │
                         │  REST Endpoints  │
                         └────────┬─────────┘
                                  │
                                  ▼
                         ┌──────────────────┐
                         │     Services     │
                         │   Business Logic │
                         └───────┬─────┬────┘
                                 │     │
                    ┌────────────┘     └─────────────┐
                    ▼                                ▼
           ┌─────────────────┐              ┌─────────────────┐
           │   Repositories  │              │  TV Maze Client │
           └────────┬────────┘              └────────┬────────┘
                    │                                │
                    ▼                                ▼
           ┌─────────────────┐              ┌─────────────────┐
           │   MongoDB Atlas │              │    TV Maze API  │
           └─────────────────┘              └─────────────────┘
```

| Layer        | Responsibility                   |
|--------------|----------------------------------|
| `controller` | REST endpoints and HTTP requests |
| `service`    | Business logic                   |
| `client`     | TV Maze integration              |
| `repository` | MongoDB persistence              |
| `dto`        | API request/response objects     |
| `model`      | MongoDB documents                |
| `config`     | Application configuration        |

## Technology Stack

| Technology          | Version / Usage          |
|---------------------|--------------------------|
| Java                | 17                       |
| Spring Boot         | 4.1.1                    |
| Spring Web MVC      | REST API                 |
| Spring Data MongoDB | Persistence              |
| MongoDB Atlas       | Database                 |
| Maven               | 3.9+                     |
| Bean Validation     | Request validation       |
| RestClient          | TV Maze HTTP integration |

## Project Structure

``` text
tvmaze-middleware/
├── README.md
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/marco/tvmaze/
│   │   │   ├── client/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── model/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── target/
```

# API

Base URL:

``` text
http://localhost:8080
```

## 1. Search Shows

``` http
GET /shows/search?query={query}
```

Example:

``` http
GET /shows/search?query=batman
```

The middleware queries TV Maze and returns:

- `id`
- `name`
- `channel`
- `summary`
- `genres`
- `comments`

Example response:

``` json
[
  {
    "id": 1,
    "name": "Example Show",
    "channel": "Example Channel",
    "summary": "Example summary",
    "genres": ["Drama", "Action"],
    "comments": []
  }
]
```

> The values above are illustrative.

## 2. Get Show

``` http
GET /shows/{showId}
```

Example:

``` http
GET /shows/1
```

### Cache behavior

1.  Search MongoDB using the show ID.
2.  If the show exists, return the cached document.
3.  Otherwise, call TV Maze.
4.  Save the result to MongoDB.
5.  Return the show with its associated comments.

``` text
GET /shows/{showId}
        │
        ▼
   Search MongoDB
      /     \
   exists   missing
     │         │
     ▼         ▼
   return   TV Maze API
                │
                ▼
          save to MongoDB
                │
                ▼
              return
```

## 3. Create Comment and Rating

``` http
POST /shows/{showId}/comments
```

Example:

``` http
POST /shows/1/comments
Content-Type: application/json
```

Request:

``` json
{
  "comment": "Excelente serie",
  "rating": 5
}
```

Response:

``` json
{
  "id": "generated-id",
  "comment": "Excelente serie",
  "rating": 5
}
```

The comment is associated with the `showId` in the URL.

# Validation

`rating` is required and must be an integer from **0 to 5**, inclusive.

A rating outside that range returns:

``` text
HTTP 400 Bad Request
```

`comment` is also required and cannot be blank.

# Comments in Responses

Comments are included in:

``` http
GET /shows/search?query={query}
```

and:

``` http
GET /shows/{showId}
```

Example:

``` json
{
  "id": 1,
  "name": "Example Show",
  "channel": "Example Channel",
  "summary": "Example summary",
  "genres": ["Drama"],
  "comments": [
    {
      "id": "64...",
      "comment": "Excelente serie",
      "rating": 5
    }
  ]
}
```

# Persistence

Two MongoDB collections are used:

### `shows`

``` text
id
name
channel
summary
genres
```

Stores TV Maze show data used by the cache.

### `comments`

``` text
id
showId
comment
rating
```

Stores comments and ratings. The relationship is:

``` text
Comment.showId → Show.id
```

Multiple comments can be associated with the same show.

# TV Maze Integration

External API communication is isolated in `TvMazeClient`.

The client uses Spring `RestClient` to perform:

``` text
Search shows
    └── TV Maze Search API

Get show by ID
    └── TV Maze Show API
```

This keeps external integration separate from controllers and
persistence.

# Configuration

MongoDB credentials are not stored in source code.

The application uses:

``` text
MONGODB_URI
```

Configuration:

``` properties
spring.application.name=tvmaze-middleware
spring.mongodb.uri=${MONGODB_URI}
```

PowerShell example:

``` powershell
$env:MONGODB_URI="mongodb+srv://<username>:<password>@<cluster>/tvmaze?retryWrites=true&w=majority"
```

> Never commit a real connection string or password.

# Running the Application

## Requirements

- Java 17+
- Maven 3.9+
- MongoDB Atlas or compatible MongoDB instance
- `MONGODB_URI` environment variable

## Build

``` powershell
mvn clean package -DskipTests
```

Full build:

``` powershell
mvn clean package
```

## Run

``` powershell
java -jar target/tvmaze-middleware-0.0.1-SNAPSHOT.jar
```

Application URL:

``` text
http://localhost:8080
```

# Quick Tests

Search:

``` powershell
Invoke-RestMethod "http://localhost:8080/shows/search?query=batman"
```

Get show:

``` powershell
Invoke-RestMethod "http://localhost:8080/shows/1"
```

Create comment:

``` powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/shows/1/comments" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{"comment":"Excelente serie","rating":5}'
```

Verify:

``` powershell
Invoke-RestMethod "http://localhost:8080/shows/1"
```

# Error Responses

| Situation         |       HTTP status |
|-------------------|------------------:|
| Successful search |          `200 OK` |
| Show found        |          `200 OK` |
| Comment created   |     `201 Created` |
| Invalid rating    | `400 Bad Request` |
| Blank comment     | `400 Bad Request` |

# Development History

The implementation was divided into incremental commits:

| Commit                               | Functionality             |
|--------------------------------------|---------------------------|
| `Implement search endpoint`          | TV Maze search            |
| `Implement show cache with MongoDB`  | Show retrieval and cache  |
| `Implement show comments and rating` | Comments and ratings      |
| `Include comments in show responses` | Comments in search/detail |
| `Configure MongoDB Atlas connection` | Atlas configuration       |
| `Add project documentation`          | README documentation      |

# Design Decisions

### MongoDB cache

MongoDB is used as a cache for TV Maze show information to reduce
repeated calls to the external API.

### Separate comments collection

Comments are stored separately and reference a show through `showId`.
This supports multiple comments per show without duplicating comment
data inside the cached show document.

### Dedicated TV Maze client

External API calls are isolated in `TvMazeClient`, keeping integration
concerns separate from business logic.

### Layered architecture

Controllers, services, repositories, DTOs, models, configuration, and
external API integration have separate responsibilities to keep the code
maintainable.

# Security

Before publishing the repository, verify that:

- No MongoDB password is present in source files.
- No `.env` file containing credentials is committed.
- No credential-bearing connection string exists in the repository.
- Credentials were not accidentally included in Git history.

The application obtains MongoDB credentials exclusively through:

``` text
MONGODB_URI
```

# Project Status

- [x] TV Maze search
- [x] Show detail
- [x] MongoDB show cache
- [x] Comments
- [x] Ratings from 0 to 5
- [x] Comments in search responses
- [x] Comments in show detail responses
- [x] Request validation
- [x] MongoDB Atlas configuration
- [x] Documentation

# Author

**Marco González**

Software Development Manager · Software Architect · Tech Lead

20+ years of experience in software development, architecture, technical
leadership, and digital transformation.
