package com.nequi.franchises.infrastructure.config;

import com.nequi.franchises.domain.port.out.BranchRepositoryPort;
import com.nequi.franchises.domain.port.out.FranchiseRepositoryPort;
import com.nequi.franchises.domain.port.out.ProductRepositoryPort;
import com.nequi.franchises.domain.service.BranchService;
import com.nequi.franchises.domain.service.FranchiseService;
import com.nequi.franchises.domain.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public FranchiseService franchiseService(FranchiseRepositoryPort franchiseRepositoryPort,
            BranchRepositoryPort branchRepositoryPort,
            ProductRepositoryPort productRepositoryPort) {
        return new FranchiseService(franchiseRepositoryPort, branchRepositoryPort, productRepositoryPort);
    }

    @Bean
    public BranchService branchService(BranchRepositoryPort branchRepositoryPort,
            FranchiseRepositoryPort franchiseRepositoryPort) {
        return new BranchService(branchRepositoryPort, franchiseRepositoryPort);
    }

    @Bean
    public ProductService productService(ProductRepositoryPort productRepositoryPort,
            BranchRepositoryPort branchRepositoryPort) {
        return new ProductService(productRepositoryPort, branchRepositoryPort);
    }
}
