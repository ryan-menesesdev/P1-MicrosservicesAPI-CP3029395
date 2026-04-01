package com.ryan.payments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
        @NotNull Long pedidoId,
        @NotNull @Positive Double valorPedido,
        @NotNull @Positive Double valorPago
) {}