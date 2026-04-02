package com.ryan.payments.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price
) {}