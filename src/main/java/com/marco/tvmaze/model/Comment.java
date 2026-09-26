package com.marco.tvmaze.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private Integer showId;
    private String comment;
    private Integer rating;
}