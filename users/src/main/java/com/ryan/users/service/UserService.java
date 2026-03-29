package com.ryan.users.service;

import com.ryan.users.dto.UserRequest;
import com.ryan.users.dto.UserResponse;
import com.ryan.users.model.User;
import com.ryan.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        User user = User.builder()
                .nome(request.nome())
                .senha(request.senha())
                .email(request.email())
                .cpf(request.cpf())
                .idade(request.idade())
                .dataNascimento(request.dataNascimento())
                .build();

        return mapToResponse(repository.save(user));
    }

    @Transactional
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getIdade(),
                user.getDataNascimento()
        );
    }
}