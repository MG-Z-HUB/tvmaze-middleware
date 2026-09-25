package com.marco.tvmaze.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TvMazeClient {

    private static final String TV_MAZE_BASE_URL = "https://api.tvmaze.com";

    private final RestClient restClient;

    public TvMazeClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(TV_MAZE_BASE_URL)
                .build();
    }

    public TvMazeSearchResult[] searchShows(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/shows")
                        .queryParam("q", query)
                        .build())
                .retrieve()
                .body(TvMazeSearchResult[].class);
    }
}