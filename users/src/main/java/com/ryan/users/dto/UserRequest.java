package com.ryan.users.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A senha é obrigatória")
        @Pattern(
                regexp = "^(?=.{8,}$)(?=.*\\d)(?=.*[^\\w\\s]).*$",
                message = "A senha deve ter no mínimo 8 caracteres e conter pelo menos um número e um símbolo"
        )
        String senha,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O formato de e-mail é inválido")
        String email,

        @CPF(message = "O CPF informado é inválido")
        String cpf,

        @PositiveOrZero(message = "A idade não pode ser negativa")
        Integer idade,

        @Past(message = "A data de nascimento deve ser uma data no passado")
        LocalDate dataNascimento
) {}
