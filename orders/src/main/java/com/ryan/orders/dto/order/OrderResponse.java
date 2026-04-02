package com.ryan.orders.dto.order;

import java.util.List;

public record OrderResponse(
    Long id,
    String status,
    Double valorTotal,
    Long usuarioId,
    List<OrderItemResponse> itens
) {}
