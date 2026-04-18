package com.fitness.activityservices.Repo;

import com.fitness.activityservices.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepo extends MongoRepository<Activity,String> {
}
