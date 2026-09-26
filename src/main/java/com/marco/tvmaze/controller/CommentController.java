package com.marco.tvmaze.controller;

import com.marco.tvmaze.dto.CommentRequest;
import com.marco.tvmaze.model.Comment;
import com.marco.tvmaze.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows/{showId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(
            @PathVariable Integer showId,
            @Valid @RequestBody CommentRequest request) {

        return commentService.createComment(showId, request);
    }
}