package com.marco.tvmaze.dto;

import lombok.Data;

@Data
public class CommentResponse {

    private String id;
    private String comment;
    private Integer rating;
}