package com.nequi.franchises.domain.service;

import com.nequi.franchises.domain.model.Branch;
import com.nequi.franchises.domain.model.Product;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

        @Mock
        private ProductRepositoryPort productRepository;

        @Mock
        private BranchRepositoryPort branchRepository;

        private ProductService service;

        @BeforeEach
        void setUp() {
                service = new ProductService(productRepository, branchRepository);
        }

    @Test
    void add_shouldFailWhenBranchNotFound() {
        when(branchRepository.existsById("bad-id")).thenReturn(Mono.just(false));

        StepVerifier.create(service.add("bad-id", "Producto A", 10))
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("Branch not found"))
                .verify();
    }

        @Test
        void add_shouldSaveProductWhenBranchExists() {
                Product saved = new Product("p1", "Producto A", 10, "b1");
                when(branchRepository.existsById("b1")).thenReturn(Mono.just(true));
                when(productRepository.save(any())).thenReturn(Mono.just(saved));

                StepVerifier.create(service.add("b1", "Producto A", 10))
                                .expectNextMatches(p -> p.getId().equals("p1")
                                                && p.getName().equals("Producto A")
                                                && p.getStock() == 10
                                                && p.getBranchId().equals("b1"))
                                .verifyComplete();
        }

    @Test
    void remove_shouldFailWhenProductNotFound() {
        when(productRepository.existsById("bad-id")).thenReturn(Mono.just(false));

        StepVerifier.create(service.remove("b1", "bad-id"))
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("Product not found"))
                .verify();
    }

    @Test
    void updateStock_shouldUpdateWhenProductExists() {
        when(productRepository.existsById("p1")).thenReturn(Mono.just(true));
        when(productRepository.updateStock("p1", 50)).thenReturn(Mono.empty());

        StepVerifier.create(service.updateStock("p1", 50))
                .verifyComplete();
    }

        @Test
        void getByFranchise_shouldReturnTopStockPerBranch() {
                Branch branch = new Branch("b1", "Sucursal Norte", "f1");
                Product topProduct = new Product("p1", "Producto Top", 100, "b1");

                when(branchRepository.findByFranchiseId("f1")).thenReturn(Flux.just(branch));
                when(productRepository.findTopByBranchIdOrderByStockDesc("b1")).thenReturn(Mono.just(topProduct));

                StepVerifier.create(service.getByFranchise("f1"))
                                .expectNextMatches(r -> r.getProductId().equals("p1")
                                                && r.getProductName().equals("Producto Top")
                                                && r.getStock() == 100
                                                && r.getBranchId().equals("b1")
                                                && r.getBranchName().equals("Sucursal Norte"))
                                .verifyComplete();
        }
}
