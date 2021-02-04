package com.uc.backend.mongo.repository;

import com.uc.backend.mongo.document.prices.AsesPerPriceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsesPerPriceRepository extends MongoRepository<AsesPerPriceDocument, String> {
}
