package com.ryan.inventory.dto.inventory;

public record InventoryResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeDisponivel
) {}