package com.nequi.franchises.domain.port.in;

import reactor.core.publisher.Mono;

public interface UpdateProductNameUseCase {
    Mono<Void> update(String productId, String name);
}
