package com.nequi.franchises.domain.service;

import com.nequi.franchises.domain.model.Franchise;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranchiseServiceTest {

    @Mock
    private FranchiseRepositoryPort franchiseRepository;

    @Mock
    private BranchRepositoryPort branchRepository;

    @Mock
    private ProductRepositoryPort productRepository;

    private FranchiseService service;

    @BeforeEach
    void setUp() {
        service = new FranchiseService(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_shouldSaveFranchiseAndReturnIt() {
        Franchise saved = new Franchise("abc123", "Franquicia Norte");
        when(franchiseRepository.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(service.create("Franquicia Norte"))
                .expectNextMatches(f -> f.getId().equals("abc123") && f.getName().equals("Franquicia Norte"))
                .verifyComplete();
    }

    @Test
    void update_shouldFailWhenFranchiseNotFound() {
        when(franchiseRepository.existsById("nonexistent")).thenReturn(Mono.just(false));

        StepVerifier.create(service.update("nonexistent", "Nuevo Nombre"))
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("Franchise not found"))
                .verify();
    }

    @Test
    void update_shouldUpdateNameWhenFranchiseExists() {
        when(franchiseRepository.existsById("abc123")).thenReturn(Mono.just(true));
        when(franchiseRepository.updateName("abc123", "Nuevo Nombre")).thenReturn(Mono.empty());

        StepVerifier.create(service.update("abc123", "Nuevo Nombre"))
                .verifyComplete();
    }
}
