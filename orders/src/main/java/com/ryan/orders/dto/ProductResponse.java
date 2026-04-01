package com.ryan.orders.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price
) {}
