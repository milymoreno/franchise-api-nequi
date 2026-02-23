package com.nequi.franchises.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchises.domain.model.Branch;
import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.infrastructure.adapter.out.persistence.document.BranchDocument;
import com.nequi.franchises.infrastructure.adapter.out.persistence.repository.MongoBranchRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private final MongoBranchRepository mongo;

    public BranchRepositoryAdapter(MongoBranchRepository mongo) {
        this.mongo = mongo;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchDocument doc = new BranchDocument(branch.getId(), branch.getName(), branch.getFranchiseId());
        return mongo.save(doc)
                .map(saved -> new Branch(saved.getId(), saved.getName(), saved.getFranchiseId()));
    }

    @Override
    public Mono<Branch> findById(String id) {
        return mongo.findById(id)
                .map(doc -> new Branch(doc.getId(), doc.getName(), doc.getFranchiseId()));
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return mongo.existsById(id);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return mongo.findByFranchiseId(franchiseId)
                .map(doc -> new Branch(doc.getId(), doc.getName(), doc.getFranchiseId()));
    }

    @Override
    public Mono<Void> updateName(String id, String name) {
        return mongo.findById(id)
                .flatMap(doc -> {
                    doc.setName(name);
                    return mongo.save(doc);
                })
                .then();
    }
}
