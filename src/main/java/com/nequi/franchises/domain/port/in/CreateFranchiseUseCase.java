package com.nequi.franchises.domain.port.in;

import com.nequi.franchises.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface CreateFranchiseUseCase {
    Mono<Franchise> create(String name);
}
