package com.nequi.franchises.infrastructure.adapter.in.web;

import com.nequi.franchises.application.dto.BranchRequest;
import com.nequi.franchises.application.dto.NameUpdateRequest;
import com.nequi.franchises.domain.model.Branch;
import com.nequi.franchises.domain.port.in.AddBranchUseCase;
import com.nequi.franchises.domain.port.in.UpdateBranchNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Branches", description = "Operations for managing branches")
public class BranchController {

    private final AddBranchUseCase addBranchUseCase;
    private final UpdateBranchNameUseCase updateBranchNameUseCase;

    public BranchController(AddBranchUseCase addBranchUseCase,
            UpdateBranchNameUseCase updateBranchNameUseCase) {
        this.addBranchUseCase = addBranchUseCase;
        this.updateBranchNameUseCase = updateBranchNameUseCase;
    }

    @PostMapping("/franchises/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a branch to a franchise")
    public Mono<Branch> add(@PathVariable String franchiseId,
            @Valid @RequestBody BranchRequest request) {
        return addBranchUseCase.add(franchiseId, request.getName());
    }

    @PatchMapping("/branches/{id}/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update branch name")
    public Mono<ResponseEntity<Void>> updateName(@PathVariable String id,
            @Valid @RequestBody NameUpdateRequest request) {
        return updateBranchNameUseCase.update(id, request.getName())
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}
