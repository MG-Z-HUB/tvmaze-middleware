package com.marco.tvmaze.client;

import lombok.Data;

@Data
public class TvMazeShow {

    private Integer id;
    private String name;
    private TvMazeNetwork network;
    private TvMazeWebChannel webChannel;
    private String summary;
    private String[] genres;
}