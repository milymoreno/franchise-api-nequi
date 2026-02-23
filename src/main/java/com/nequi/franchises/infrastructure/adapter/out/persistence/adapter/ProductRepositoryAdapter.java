package com.nequi.franchises.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchises.domain.model.Product;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import com.nequi.franchises.infrastructure.adapter.out.persistence.document.ProductDocument;
import com.nequi.franchises.infrastructure.adapter.out.persistence.repository.MongoProductRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final MongoProductRepository mongo;

    public ProductRepositoryAdapter(MongoProductRepository mongo) {
        this.mongo = mongo;
    }

    private Product toDomain(ProductDocument doc) {
        return new Product(doc.getId(), doc.getName(), doc.getStock(), doc.getBranchId());
    }

    @Override
    public Mono<Product> save(Product product) {
        ProductDocument doc = new ProductDocument(product.getId(), product.getName(),
                product.getStock(), product.getBranchId());
        return mongo.save(doc).map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return mongo.deleteById(id);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return mongo.existsById(id);
    }

    @Override
    public Mono<Void> updateStock(String id, int stock) {
        return mongo.findById(id)
                .flatMap(doc -> {
                    doc.setStock(stock);
                    return mongo.save(doc);
                })
                .then();
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

    @Override
    public Flux<Product> findByBranchId(String branchId) {
        return mongo.findByBranchId(branchId).map(this::toDomain);
    }

    @Override
    public Mono<Product> findTopByBranchIdOrderByStockDesc(String branchId) {
        return mongo.findByBranchIdOrderByStockDesc(branchId).next().map(this::toDomain);
    }
}
