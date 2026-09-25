package com.marco.tvmaze.client;

import lombok.Data;

@Data
public class TvMazeSearchResult {

    private Double score;
    private TvMazeShow show;
}