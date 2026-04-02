package com.ryan.payments.dto;

public record PaymentResponse(
        Long pagamentoId,
        Long pedidoId,
        String status
) {}
