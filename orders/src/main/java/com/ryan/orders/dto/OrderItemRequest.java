package com.ryan.orders.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull Long produtoId,
        @NotNull Integer quantidade
) {}
