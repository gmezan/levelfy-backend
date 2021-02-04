package com.uc.backend.mongo.repository;

import com.uc.backend.mongo.document.MongoCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCourseRepository extends MongoRepository<MongoCourse, String> {

}
