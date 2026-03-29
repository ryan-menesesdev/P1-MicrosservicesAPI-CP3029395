package com.ryan.catalog.service;

import com.ryan.catalog.dto.ProductRequest;
import com.ryan.catalog.dto.ProductResponse;
import com.ryan.catalog.model.Product;
import com.ryan.catalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional()
    public List<ProductResponse> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse).toList();
    }

    @Transactional()
    public ProductResponse findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .nome(request.nome())
                .observacao(request.observacao())
                .valorIndividual(request.valorIndividual())
                .build();

        return mapToResponse(repository.save(product));
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getNome(),
                product.getObservacao(),
                product.getValorIndividual()
        );
    }
}