package com.ryan.orders.dto.payment;

public record PaymentResponse(
        Long pagamentoId,
        Long pedidoId,
        String status,
        Double valor
) {}
