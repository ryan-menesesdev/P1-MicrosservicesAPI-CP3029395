package com.ryan.orders.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ConfirmPaymentRequest(
    @NotNull(message = "O valor pago é obrigatório")
    @Positive(message = "O valor pago deve ser positivo")
    Double valorPago
) {}