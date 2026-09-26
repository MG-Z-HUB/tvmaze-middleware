package com.marco.tvmaze.service;

import com.marco.tvmaze.dto.CommentRequest;
import com.marco.tvmaze.model.Comment;
import com.marco.tvmaze.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Integer showId, CommentRequest request) {

        Comment comment = new Comment();
        comment.setShowId(showId);
        comment.setComment(request.getComment());
        comment.setRating(request.getRating());

        return commentRepository.save(comment);
    }
}