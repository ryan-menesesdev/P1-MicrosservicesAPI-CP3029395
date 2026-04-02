package com.ryan.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequest(
        @NotBlank(message = "O nome do produto não pode estar em branco")
        String nome,

        @NotBlank(message = "A observação é obrigatória")
        String observacao,

        @NotNull(message = "O valor individual é obrigatório")
        @Positive(message = "O valor individual deve ser maior que zero")
        Double valorIndividual
) {}