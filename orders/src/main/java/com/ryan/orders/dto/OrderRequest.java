package com.ryan.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderRequest(
        @NotNull Long usuarioId,
        @NotNull @Positive Double valorPago,
        @NotEmpty List<OrderItemRequest> itens
) {}