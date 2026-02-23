package com.nequi.franchises.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchises.domain.model.Franchise;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
import com.nequi.franchises.infrastructure.adapter.out.persistence.document.FranchiseDocument;
import com.nequi.franchises.infrastructure.adapter.out.persistence.repository.MongoFranchiseRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final MongoFranchiseRepository mongo;

    public FranchiseRepositoryAdapter(MongoFranchiseRepository mongo) {
        this.mongo = mongo;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseDocument doc = new FranchiseDocument(franchise.getId(), franchise.getName());
        return mongo.save(doc)
                .map(saved -> new Franchise(saved.getId(), saved.getName()));
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return mongo.findById(id)
                .map(doc -> new Franchise(doc.getId(), doc.getName()));
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return mongo.existsById(id);
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
