package com.nequi.franchises.domain.service;

import com.nequi.franchises.domain.model.Branch;
import com.nequi.franchises.domain.port.in.AddBranchUseCase;
import com.nequi.franchises.domain.port.in.UpdateBranchNameUseCase;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
import reactor.core.publisher.Mono;

public class BranchService implements AddBranchUseCase, UpdateBranchNameUseCase {

    private final BranchRepositoryPort branchRepository;
    private final FranchiseRepositoryPort franchiseRepository;

    public BranchService(BranchRepositoryPort branchRepository,
            FranchiseRepositoryPort franchiseRepository) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    @Override
    public Mono<Branch> add(String franchiseId, String name) {
        return franchiseRepository.existsById(franchiseId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Franchise not found: " + franchiseId));
                    return branchRepository.save(new Branch(null, name, franchiseId));
                });
    }

    @Override
    public Mono<Void> update(String id, String name) {
        return branchRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Branch not found: " + id));
                    return branchRepository.updateName(id, name);
                });
    }
}
