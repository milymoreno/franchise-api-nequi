package com.nequi.franchises.domain.port.in;

import reactor.core.publisher.Mono;

public interface UpdateStockUseCase {
    Mono<Void> updateStock(String productId, int stock);
}
