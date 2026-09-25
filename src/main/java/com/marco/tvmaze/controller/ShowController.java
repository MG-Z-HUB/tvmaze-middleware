package com.marco.tvmaze.controller;

import com.marco.tvmaze.dto.ShowResponse;
import com.marco.tvmaze.service.TvMazeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final TvMazeService tvMazeService;

    public ShowController(TvMazeService tvMazeService) {
        this.tvMazeService = tvMazeService;
    }

    @GetMapping("/search")
    public List<ShowResponse> searchShows(
            @RequestParam String query) {

        return tvMazeService.searchShows(query);
    }
}
