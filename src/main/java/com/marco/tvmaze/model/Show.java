package com.marco.tvmaze.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "shows")
public class Show {

    @Id
    private Integer id;

    private String name;
    private String channel;
    private String summary;
    private String[] genres;
}