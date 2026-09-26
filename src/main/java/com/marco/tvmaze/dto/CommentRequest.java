package com.marco.tvmaze.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotBlank
    private String comment;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer rating;
}