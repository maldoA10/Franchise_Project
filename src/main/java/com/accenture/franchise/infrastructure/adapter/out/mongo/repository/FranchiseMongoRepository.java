package com.accenture.franchise.infrastructure.adapter.out.mongo.repository;

import com.accenture.franchise.infrastructure.adapter.out.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseMongoRepository
    extends ReactiveMongoRepository<FranchiseDocument, String> {
}