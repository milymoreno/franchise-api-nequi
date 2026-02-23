package com.nequi.franchises.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchises.infrastructure.adapter.out.persistence.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MongoProductRepository extends ReactiveMongoRepository<ProductDocument, String> {
    Flux<ProductDocument> findByBranchId(String branchId);

    Flux<ProductDocument> findByBranchIdOrderByStockDesc(String branchId);
}
