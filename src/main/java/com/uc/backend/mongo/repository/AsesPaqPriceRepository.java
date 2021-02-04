package com.uc.backend.mongo.repository;

import com.uc.backend.mongo.document.prices.AsesPaqPriceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsesPaqPriceRepository extends MongoRepository<AsesPaqPriceDocument, String> {
}
