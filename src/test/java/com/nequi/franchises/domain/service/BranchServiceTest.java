package com.nequi.franchises.domain.service;

import com.nequi.franchises.domain.model.Branch;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
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
class BranchServiceTest {

    @Mock
    private BranchRepositoryPort branchRepository;

    @Mock
    private FranchiseRepositoryPort franchiseRepository;

    private BranchService service;

    @BeforeEach
    void setUp() {
        service = new BranchService(branchRepository, franchiseRepository);
    }

    @Test
    void add_shouldFailWhenFranchiseNotFound() {
        when(franchiseRepository.existsById("bad-id")).thenReturn(Mono.just(false));

        StepVerifier.create(service.add("bad-id", "Sucursal Norte"))
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("Franchise not found"))
                .verify();
    }

    @Test
    void add_shouldSaveBranchWhenFranchiseExists() {
        Branch saved = new Branch("b1", "Sucursal Norte", "f1");
        when(franchiseRepository.existsById("f1")).thenReturn(Mono.just(true));
        when(branchRepository.save(any())).thenReturn(Mono.just(saved));

        StepVerifier.create(service.add("f1", "Sucursal Norte"))
                .expectNextMatches(b -> b.getId().equals("b1")
                        && b.getName().equals("Sucursal Norte")
                        && b.getFranchiseId().equals("f1"))
                .verifyComplete();
    }

    @Test
    void update_shouldFailWhenBranchNotFound() {
        when(branchRepository.existsById("bad-id")).thenReturn(Mono.just(false));

        StepVerifier.create(service.update("bad-id", "Nuevo Nombre"))
                .expectErrorMatches(e -> e instanceof RuntimeException
                        && e.getMessage().contains("Branch not found"))
                .verify();
    }

    @Test
    void update_shouldUpdateNameWhenBranchExists() {
        when(branchRepository.existsById("b1")).thenReturn(Mono.just(true));
        when(branchRepository.updateName("b1", "Sucursal Sur")).thenReturn(Mono.empty());

        StepVerifier.create(service.update("b1", "Sucursal Sur"))
                .verifyComplete();
    }
}
