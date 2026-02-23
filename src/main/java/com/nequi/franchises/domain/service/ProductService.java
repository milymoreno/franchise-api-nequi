package com.nequi.franchises.domain.service;

import com.nequi.franchises.application.dto.MaxStockProductResponse;
import com.nequi.franchises.domain.model.Product;
import com.nequi.franchises.domain.port.in.*;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductService implements AddProductUseCase, RemoveProductUseCase,
        UpdateStockUseCase, UpdateProductNameUseCase, GetMaxStockProductsUseCase {

    private final ProductRepositoryPort productRepository;
    private final BranchRepositoryPort branchRepository;

    public ProductService(ProductRepositoryPort productRepository,
            BranchRepositoryPort branchRepository) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public Mono<Product> add(String branchId, String name, int stock) {
        return branchRepository.existsById(branchId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Branch not found: " + branchId));
                    return productRepository.save(new Product(null, name, stock, branchId));
                });
    }

    @Override
    public Mono<Void> remove(String branchId, String productId) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Product not found: " + productId));
                    return productRepository.deleteById(productId);
                });
    }

    @Override
    public Mono<Void> updateStock(String productId, int stock) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Product not found: " + productId));
                    return productRepository.updateStock(productId, stock);
                });
    }

    @Override
    public Mono<Void> update(String productId, String name) {
        return productRepository.existsById(productId)
                .flatMap(exists -> {
                    if (!exists)
                        return Mono.error(new RuntimeException("Product not found: " + productId));
                    return productRepository.updateName(productId, name);
                });
    }

    @Override
    public Flux<MaxStockProductResponse> getByFranchise(String franchiseId) {
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch -> productRepository.findTopByBranchIdOrderByStockDesc(branch.getId())
                        .map(product -> new MaxStockProductResponse(
                                product.getId(),
                                product.getName(),
                                product.getStock(),
                                branch.getId(),
                                branch.getName())));
    }
}
