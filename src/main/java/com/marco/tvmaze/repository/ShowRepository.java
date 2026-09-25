package com.marco.tvmaze.repository;

import com.marco.tvmaze.model.Show;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShowRepository extends MongoRepository<Show, Integer> {
}