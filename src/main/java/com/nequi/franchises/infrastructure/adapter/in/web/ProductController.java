package com.nequi.franchises.infrastructure.adapter.in.web;

import com.nequi.franchises.application.dto.*;
import com.nequi.franchises.domain.model.Product;
import com.nequi.franchises.domain.port.in.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Products", description = "Operations for managing products")
public class ProductController {

    private final AddProductUseCase addProductUseCase;
    private final RemoveProductUseCase removeProductUseCase;
    private final UpdateStockUseCase updateStockUseCase;
    private final UpdateProductNameUseCase updateProductNameUseCase;
    private final GetMaxStockProductsUseCase getMaxStockProductsUseCase;

    public ProductController(AddProductUseCase addProductUseCase,
            RemoveProductUseCase removeProductUseCase,
            UpdateStockUseCase updateStockUseCase,
            UpdateProductNameUseCase updateProductNameUseCase,
            GetMaxStockProductsUseCase getMaxStockProductsUseCase) {
        this.addProductUseCase = addProductUseCase;
        this.removeProductUseCase = removeProductUseCase;
        this.updateStockUseCase = updateStockUseCase;
        this.updateProductNameUseCase = updateProductNameUseCase;
        this.getMaxStockProductsUseCase = getMaxStockProductsUseCase;
    }

    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a product to a branch")
    public Mono<Product> add(@PathVariable String branchId,
            @Valid @RequestBody ProductRequest request) {
        return addProductUseCase.add(branchId, request.getName(), request.getStock());
    }

    @DeleteMapping("/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove a product from a branch")
    public Mono<ResponseEntity<Void>> remove(@PathVariable String branchId,
            @PathVariable String productId) {
        return removeProductUseCase.remove(branchId, productId)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }

    @PatchMapping("/products/{id}/stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update product stock")
    public Mono<ResponseEntity<Void>> updateStock(@PathVariable String id,
            @Valid @RequestBody StockUpdateRequest request) {
        return updateStockUseCase.updateStock(id, request.getStock())
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }

    @PatchMapping("/products/{id}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update product name")
    public Mono<ResponseEntity<Void>> updateName(@PathVariable String id,
            @Valid @RequestBody NameUpdateRequest request) {
        return updateProductNameUseCase.update(id, request.getName())
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }

    @GetMapping("/franchises/{franchiseId}/top-stock-products")
    @Operation(summary = "Get the product with most stock per branch for a franchise")
    public Flux<MaxStockProductResponse> getTopStockProducts(@PathVariable String franchiseId) {
        return getMaxStockProductsUseCase.getByFranchise(franchiseId);
    }
}
