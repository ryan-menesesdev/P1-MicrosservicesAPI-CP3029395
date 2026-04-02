package com.ryan.payments.dto;

public record InventoryResponse(
        Long productId,
        String productName,
        Integer availableQuantity
) {}