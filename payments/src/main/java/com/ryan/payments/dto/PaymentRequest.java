package com.ryan.payments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(
        @NotNull(message = "O ID do pedido é obrigatório")
        Long pedidoId,

        @NotNull(message = "O valor do pedido é obrigatório")
        @Positive(message = "O valor do pedido deve ser maior que zero")
        Double valorPedido,

        @NotNull(message = "O valor pago é obrigatório")
        @Positive(message = "O valor pago deve ser maior que zero")
        Double valorPago
) {}