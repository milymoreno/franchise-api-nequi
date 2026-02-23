package com.nequi.franchises.infrastructure.adapter.in.web;

import com.nequi.franchises.application.dto.FranchiseRequest;
import com.nequi.franchises.application.dto.FranchiseResponse;
import com.nequi.franchises.application.dto.NameUpdateRequest;
import com.nequi.franchises.domain.model.Franchise;
import com.nequi.franchises.domain.port.in.CreateFranchiseUseCase;
import com.nequi.franchises.domain.port.in.GetFranchiseByIdUseCase;
import com.nequi.franchises.domain.port.in.UpdateFranchiseNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franchises")
@Tag(name = "Franchises", description = "Operations for managing franchises")
public class FranchiseController {

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final UpdateFranchiseNameUseCase updateFranchiseNameUseCase;
    private final GetFranchiseByIdUseCase getFranchiseByIdUseCase;

    public FranchiseController(CreateFranchiseUseCase createFranchiseUseCase,
            UpdateFranchiseNameUseCase updateFranchiseNameUseCase,
            GetFranchiseByIdUseCase getFranchiseByIdUseCase) {
        this.createFranchiseUseCase = createFranchiseUseCase;
        this.updateFranchiseNameUseCase = updateFranchiseNameUseCase;
        this.getFranchiseByIdUseCase = getFranchiseByIdUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new franchise")
    public Mono<Franchise> create(@Valid @RequestBody FranchiseRequest request) {
        return createFranchiseUseCase.create(request.getName());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get franchise by ID with its branches and products")
    public Mono<FranchiseResponse> getById(@PathVariable String id) {
        return getFranchiseByIdUseCase.getById(id);
    }

    @PatchMapping("/{id}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update franchise name")
    public Mono<ResponseEntity<Void>> updateName(@PathVariable String id,
            @Valid @RequestBody NameUpdateRequest request) {
        return updateFranchiseNameUseCase.update(id, request.getName())
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}
