package com.nequi.franchises.domain.port.in;

import com.nequi.franchises.application.dto.MaxStockProductResponse;
import reactor.core.publisher.Flux;

public interface GetMaxStockProductsUseCase {
    Flux<MaxStockProductResponse> getByFranchise(String franchiseId);
}
