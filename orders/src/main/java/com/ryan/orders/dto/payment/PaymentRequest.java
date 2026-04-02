package com.ryan.orders.dto.payment;

public record PaymentRequest(
        Long pedidoId,
        Double valorPedido,
        Double valorPago
) {}
