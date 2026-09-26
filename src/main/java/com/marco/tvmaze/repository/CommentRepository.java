package com.marco.tvmaze.repository;

import com.marco.tvmaze.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByShowId(Integer showId);
}
