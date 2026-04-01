package com.ryan.payments.service;

import com.ryan.payments.dto.InventoryResponse;
import com.ryan.payments.model.Inventory;
import com.ryan.payments.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional()
    public InventoryResponse findByProductId(Long productId) {
        Inventory inventory = repository.findByProdutoId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        return new InventoryResponse(
                inventory.getProdutoId(),
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
    public InventoryResponse deductInventory(Long productId, Integer quantidade) {
        Inventory inventory = repository.findByProdutoId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        if (inventory.getQuantidadeDisponivel() < quantidade) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }

        inventory.setQuantidadeDisponivel(inventory.getQuantidadeDisponivel() - quantidade);
        Inventory savedInventory = repository.save(inventory);

        return new InventoryResponse(
                savedInventory.getProdutoId(),
                savedInventory.getQuantidadeDisponivel()
        );
    }

    @Transactional
    public InventoryResponse initializeStock(Long productId, Integer initialQuantity) {
        Optional<Inventory> existing = repository.findByProdutoId(productId);
        if (existing.isPresent()) {
            throw new RuntimeException("Stock registry already exists for product: " + productId);
        }

        Inventory inventory = Inventory.builder()
                .produtoId(productId)
                .quantidadeDisponivel(initialQuantity)
                .build();

        Inventory savedInventory = repository.save(inventory);

        return new InventoryResponse(
                savedInventory.getProdutoId(),
                savedInventory.getQuantidadeDisponivel()
        );
    }
}