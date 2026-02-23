package com.nequi.franchises.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchises.infrastructure.adapter.out.persistence.document.BranchDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MongoBranchRepository extends ReactiveMongoRepository<BranchDocument, String> {
    Flux<BranchDocument> findByFranchiseId(String franchiseId);
}
