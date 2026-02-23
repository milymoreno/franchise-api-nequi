package com.nequi.franchises.domain.port.out;

import com.nequi.franchises.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {
    Mono<Product> save(Product product);

    Mono<Void> deleteById(String id);

    Mono<Boolean> existsById(String id);

    Mono<Void> updateStock(String id, int stock);

    Mono<Void> updateName(String id, String name);

    Flux<Product> findByBranchId(String branchId);

    Mono<Product> findTopByBranchIdOrderByStockDesc(String branchId);
}
