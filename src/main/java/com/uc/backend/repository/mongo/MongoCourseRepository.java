package com.uc.backend.repository.mongo;

import com.uc.backend.service.mongo.MongoCourse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCourseRepository extends MongoRepository<MongoCourse, String> {

}
