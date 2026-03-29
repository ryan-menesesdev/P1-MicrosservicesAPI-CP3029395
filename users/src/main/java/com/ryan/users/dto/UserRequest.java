package com.ryan.users.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank String nome,
        @Pattern(
                regexp = "^(?=.{8,}$)(?=.*\\d)(?=.*[^\\w\\s]).*$",
                message = "Senha deve ter no mínimo 8 caracteres e conter pelo menos um número e um símbolo"
        )
        @NotBlank String senha,
        @NotBlank @Email String email,
        @CPF String cpf,
        @PositiveOrZero Integer idade,
        @Past LocalDate dataNascimento
) {}
