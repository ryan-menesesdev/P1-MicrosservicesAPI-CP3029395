package com.ryan.users.dto;

import java.time.LocalDate;

public record UserResponse(
        Long id,
        String nome,
        String email,
        Integer idade,
        LocalDate dataNascimento
) {}
