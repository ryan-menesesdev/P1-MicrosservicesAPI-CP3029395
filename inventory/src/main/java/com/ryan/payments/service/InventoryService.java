package com.ryan.payments.service;

import com.ryan.payments.controller.handler.exception.InsufficientStockException;
import com.ryan.payments.controller.handler.exception.InventoryNotFoundException;
import com.ryan.payments.dto.inventory.InventoryRequest;
import com.ryan.payments.dto.inventory.InventoryResponse;
import com.ryan.payments.dto.product.ProductResponse;
import com.ryan.payments.feign.ProductFeignClient;
import com.ryan.payments.model.Inventory;
import com.ryan.payments.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository repository;
    private final ProductFeignClient productFeignClient;

    public InventoryService(InventoryRepository repository, ProductFeignClient productFeignClient) {
        this.repository = repository;
        this.productFeignClient = productFeignClient;
    }

    @Transactional
    public InventoryResponse findByProductId(Long productId) {
        Inventory inventory = getOrCreateInventory(productId);
        ProductResponse product = productFeignClient.getProductById(productId);

        return new InventoryResponse(
                inventory.getProdutoId(),
                product.nome(),
                inventory.getQuantidadeDisponivel()
        );
    }

    @Transactional()
    public boolean checkAvailability(Long productId, Integer quantity) {
        return repository.findByProdutoId(productId)
                .map(inv -> inv.getQuantidadeDisponivel() >= quantity)
                .orElse(false);
    }

    @Transactional
    public InventoryResponse deductInventory(Long productId, InventoryRequest request) {
        Inventory inventory = repository.findByProdutoId(productId)
                .orElseThrow(() -> new InventoryNotFoundException("Estoque não inicializado para o produto: " + productId));

        if (inventory.getQuantidadeDisponivel() < request.quantidade()) {
            throw new InsufficientStockException("Estoque insuficiente para o produto: " + productId);
        }

        inventory.setQuantidadeDisponivel(inventory.getQuantidadeDisponivel() - request.quantidade());
        Inventory savedInventory = repository.save(inventory);

        ProductResponse product = productFeignClient.getProductById(productId);

        return new InventoryResponse(
                savedInventory.getProdutoId(),
                product.nome(),
                savedInventory.getQuantidadeDisponivel()
        );
    }

    @Transactional
    public InventoryResponse addToInventory(Long productId, InventoryRequest request) {
        Inventory inventory = getOrCreateInventory(productId);

        inventory.setQuantidadeDisponivel(request.quantidade());
        Inventory savedInventory = repository.save(inventory);

        ProductResponse product = productFeignClient.getProductById(productId);

        return new InventoryResponse(
                savedInventory.getProdutoId(),
                product.nome(),
                savedInventory.getQuantidadeDisponivel()
        );
    }

    private Inventory getOrCreateInventory(Long productId) {
        return repository.findByProdutoId(productId).orElseGet(() -> {
            productFeignClient.getProductById(productId);

            Inventory newInventory = new Inventory();
            newInventory.setProdutoId(productId);
            newInventory.setQuantidadeDisponivel(0);

            return repository.save(newInventory);
        });
    }
}