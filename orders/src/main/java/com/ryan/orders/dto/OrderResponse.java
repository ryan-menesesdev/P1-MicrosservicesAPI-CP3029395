package com.ryan.orders.dto;

import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        Double valorTotal,
        Long usuarioId,
        List<OrderItemResponse> itens
) {}
