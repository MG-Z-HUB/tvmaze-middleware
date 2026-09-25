package com.marco.tvmaze.service;

import com.marco.tvmaze.client.TvMazeClient;
import com.marco.tvmaze.client.TvMazeSearchResult;
import com.marco.tvmaze.client.TvMazeShow;
import com.marco.tvmaze.dto.ShowResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TvMazeService {

    private final TvMazeClient tvMazeClient;

    public TvMazeService(TvMazeClient tvMazeClient) {
        this.tvMazeClient = tvMazeClient;
    }

    public List<ShowResponse> searchShows(String query) {

        TvMazeSearchResult[] results = tvMazeClient.searchShows(query);

        return Arrays.stream(results)
                .map(TvMazeSearchResult::getShow)
                .map(this::toShowResponse)
                .toList();
    }

    private ShowResponse toShowResponse(TvMazeShow show) {

        ShowResponse response = new ShowResponse();

        response.setId(show.getId());
        response.setName(show.getName());
        response.setChannel(resolveChannel(show));
        response.setSummary(show.getSummary());
        response.setGenres(show.getGenres());

        return response;
    }

    private String resolveChannel(TvMazeShow show) {

        if (show.getNetwork() != null
                && show.getNetwork().getName() != null) {
            return show.getNetwork().getName();
        }

        if (show.getWebChannel() != null
                && show.getWebChannel().getName() != null) {
            return show.getWebChannel().getName();
        }

        return null;
    }

    public ShowResponse getShowById(Integer showId) {

        TvMazeShow show = tvMazeClient.getShowById(showId);
        return toShowResponse(show);
    }

}