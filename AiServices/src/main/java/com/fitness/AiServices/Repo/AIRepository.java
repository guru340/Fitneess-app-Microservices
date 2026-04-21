package com.fitness.AiServices.Repo;

import com.fitness.AiServices.model.Recommdentation;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AIRepository extends MongoRepository<Recommdentation ,String> {

    Optional<Recommdentation> findByAcitivityId(String activityId);

    List<Recommdentation> findByUserId(String userId);
}
