package com.ryan.payments.dto.inventory;

public record InventoryResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeDisponivel
) {}