package com.uc.backend.mongo.repository;

import com.uc.backend.mongo.document.prices.MarPriceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarPriceRepository extends MongoRepository<MarPriceDocument, String> {
}
