package com.ryan.orders.dto.order;

public record OrderItemResponse(
    Long id,
    Long produtoId,
    Integer quantidade,
    Double precoCompra
) {}
