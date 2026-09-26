package com.marco.tvmaze.service;

import com.marco.tvmaze.client.TvMazeClient;
import com.marco.tvmaze.client.TvMazeSearchResult;
import com.marco.tvmaze.client.TvMazeShow;
import com.marco.tvmaze.dto.ShowResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.marco.tvmaze.model.Show;
import com.marco.tvmaze.repository.ShowRepository;
import com.marco.tvmaze.repository.CommentRepository;

import com.marco.tvmaze.dto.CommentResponse;
import com.marco.tvmaze.repository.CommentRepository;
import java.util.stream.Collectors;


@Service
public class TvMazeService {

    private final TvMazeClient tvMazeClient;
    private final ShowRepository showRepository;
    private final CommentRepository commentRepository;

    public TvMazeService(
            TvMazeClient tvMazeClient,
            ShowRepository showRepository,
            CommentRepository commentRepository) {

        this.tvMazeClient = tvMazeClient;
        this.showRepository = showRepository;
        this.commentRepository = commentRepository;
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
        response.setComments(getComments(show.getId()));

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

        return showRepository.findById(showId)
                .map(this::toShowResponse)
                .orElseGet(() -> {

                    TvMazeShow tvMazeShow =
                            tvMazeClient.getShowById(showId);

                    Show show = toShow(tvMazeShow);

                    Show savedShow = showRepository.save(show);

                    return toShowResponse(savedShow);
                });
    }

    private Show toShow(TvMazeShow tvMazeShow) {

        Show show = new Show();

        show.setId(tvMazeShow.getId());
        show.setName(tvMazeShow.getName());
        show.setChannel(resolveChannel(tvMazeShow));
        show.setSummary(tvMazeShow.getSummary());
        show.setGenres(tvMazeShow.getGenres());

        return show;
    }


    private ShowResponse toShowResponse(Show show) {

        ShowResponse response = new ShowResponse();

        response.setId(show.getId());
        response.setName(show.getName());
        response.setChannel(show.getChannel());
        response.setSummary(show.getSummary());
        response.setGenres(show.getGenres());
        response.setComments(getComments(show.getId()));

        return response;
    }

    private List<CommentResponse> getComments(Integer showId) {

    return commentRepository.findByShowId(showId)
            .stream()
            .map(comment -> {
                CommentResponse response = new CommentResponse();
                response.setId(comment.getId());
                response.setComment(comment.getComment());
                response.setRating(comment.getRating());
                return response;
            })
            .toList();
    }

}