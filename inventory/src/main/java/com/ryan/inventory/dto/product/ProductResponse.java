package com.ryan.inventory.dto.product;

public record ProductResponse(
    Long id,
    String nome,
    String observacao,
    Double valorIndividual
) {}