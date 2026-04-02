package com.ryan.catalog.dto;

public record ProductResponse(
        Long id,
        String nome,
        String observacao,
        Double valorIndividual
) {}
