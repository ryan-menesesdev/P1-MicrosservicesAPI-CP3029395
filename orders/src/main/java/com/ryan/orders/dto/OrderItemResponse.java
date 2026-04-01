package com.ryan.orders.dto;

public record OrderItemResponse(
        Long id,
        Long produtoId,
        Integer quantidade,
        Double precoCompra
) {}
