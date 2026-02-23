package com.nequi.franchises.domain.port.out;

import com.nequi.franchises.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepositoryPort {
    Mono<Branch> save(Branch branch);

    Mono<Branch> findById(String id);

    Mono<Boolean> existsById(String id);

    Flux<Branch> findByFranchiseId(String franchiseId);

    Mono<Void> updateName(String id, String name);
}
