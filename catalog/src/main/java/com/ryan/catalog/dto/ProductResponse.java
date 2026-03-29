package com.ryan.catalog.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price
) {}
