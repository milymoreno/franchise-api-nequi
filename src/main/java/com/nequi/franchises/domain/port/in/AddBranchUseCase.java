package com.nequi.franchises.domain.port.in;

import com.nequi.franchises.domain.model.Branch;
import reactor.core.publisher.Mono;

public interface AddBranchUseCase {
    Mono<Branch> add(String franchiseId, String name);
}
