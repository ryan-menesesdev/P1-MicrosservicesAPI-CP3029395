package com.ryan.users.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_tb")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String senha;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "cpf", unique = true)
    private String cpf;

    private Integer idade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
}

