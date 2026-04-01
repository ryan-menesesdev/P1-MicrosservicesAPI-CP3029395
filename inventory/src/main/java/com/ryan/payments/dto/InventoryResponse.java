package com.ryan.payments.dto;

public record InventoryResponse(
        Long produtoId,
        Integer quantidadeDisponivel
) {}
