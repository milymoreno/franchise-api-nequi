package com.nequi.franchises.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchises.infrastructure.adapter.out.persistence.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFranchiseRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
