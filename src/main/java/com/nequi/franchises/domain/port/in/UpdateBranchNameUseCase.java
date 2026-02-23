package com.nequi.franchises.domain.port.in;

import reactor.core.publisher.Mono;

public interface UpdateBranchNameUseCase {
    Mono<Void> update(String id, String name);
}
