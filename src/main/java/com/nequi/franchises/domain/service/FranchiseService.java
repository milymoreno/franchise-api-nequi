package com.nequi.franchises.domain.service;

import com.nequi.franchises.application.dto.BranchResponse;
import com.nequi.franchises.application.dto.FranchiseResponse;
import com.nequi.franchises.application.dto.ProductResponse;
import com.nequi.franchises.domain.model.Franchise;
import com.nequi.franchises.domain.port.in.CreateFranchiseUseCase;
import com.nequi.franchises.domain.port.in.GetFranchiseByIdUseCase;
import com.nequi.franchises.domain.port.in.UpdateFranchiseNameUseCase;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Mono;

public class FranchiseService implements CreateFranchiseUseCase,
        UpdateFranchiseNameUseCase, GetFranchiseByIdUseCase {

    private final FranchiseRepositoryPort repository;
    private final BranchRepositoryPort branchRepository;
    private final ProductRepositoryPort productRepository;

    public FranchiseService(FranchiseRepositoryPort repository,
            BranchRepositoryPort branchRepository,
            ProductRepositoryPort productRepository) {
        this.repository = repository;
        this.branchRepository = branchRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Franchise> create(String name) {
        return repository.save(new Franchise(null, name));
    }

    @Override
    public Mono<Void> update(String id, String name) {
        return repository.existsById(id)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Franchise not found: " + id));
                    return repository.updateName(id, name);
                });
    }

    @Override
    public Mono<FranchiseResponse> getById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found: " + id)))
                .flatMap(franchise -> branchRepository.findByFranchiseId(franchise.getId())
                        .flatMap(branch -> productRepository.findByBranchId(branch.getId())
                                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getStock()))
                                .collectList()
                                .map(products -> new BranchResponse(branch.getId(), branch.getName(), products)))
                        .collectList()
                        .map(branches -> new FranchiseResponse(franchise.getId(), franchise.getName(), branches)));
    }
}
