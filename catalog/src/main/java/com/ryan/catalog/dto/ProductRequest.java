package com.ryan.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank String nome,
        @NotBlank String observacao,
        @NotNull @Positive Double valorIndividual
) {}