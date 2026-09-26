package com.marco.tvmaze.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShowResponse {

    private Integer id;
    private String name;
    private String channel;
    private String summary;
    private String[] genres;
    private List<CommentResponse> comments;
}