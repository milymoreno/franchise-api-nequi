package com.nequi.franchises.domain.port.in;

import reactor.core.publisher.Mono;

public interface RemoveProductUseCase {
    Mono<Void> remove(String branchId, String productId);
}
