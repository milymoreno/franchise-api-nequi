package com.nequi.franchises.domain.port.out;

import com.nequi.franchises.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepositoryPort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
    Mono<Boolean> existsById(String id);
    Mono<Void> updateName(String id, String name);
}
