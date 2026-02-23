package com.nequi.franchises.domain.port.in;

import com.nequi.franchises.domain.model.Product;
import reactor.core.publisher.Mono;

public interface AddProductUseCase {
    Mono<Product> add(String branchId, String name, int stock);
}
