package com.nequi.franchises.domain.port.in;

import com.nequi.franchises.application.dto.FranchiseResponse;
import reactor.core.publisher.Mono;

public interface GetFranchiseByIdUseCase {
    Mono<FranchiseResponse> getById(String id);
}
