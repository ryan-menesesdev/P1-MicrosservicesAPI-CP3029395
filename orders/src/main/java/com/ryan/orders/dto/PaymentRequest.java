package com.ryan.orders.dto;

public record PaymentRequest(
        Long pedidoId,
        Double valorPedido,
        Double valorPago
) {}
