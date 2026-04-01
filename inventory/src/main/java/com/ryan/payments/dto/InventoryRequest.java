package com.ryan.payments.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryRequest(
        @NotNull @Positive Integer quantidade
) {}
