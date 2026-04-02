package com.ryan.users.service;

import com.ryan.users.controller.handler.exception.CpfAlreadyExistentException;
import com.ryan.users.controller.handler.exception.EmailAlreadyExistentException;
import com.ryan.users.controller.handler.exception.UnderAgeException;
import com.ryan.users.controller.handler.exception.UserNotFoundException;
import com.ryan.users.dto.UserRequest;
import com.ryan.users.dto.UserResponse;
import com.ryan.users.model.User;
import com.ryan.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UserResponse create(UserRequest request) {

        if (request.email() != null && repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistentException(request.email());
        }

        if (request.cpf() != null && repository.existsByCpf(request.cpf())) {
            throw new CpfAlreadyExistentException(request.cpf());
        }

        if (request.idade() != null && request.idade() < 18) {
            throw new UnderAgeException("Idade não pode ser menor de 18 anos");
        }

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

    @Transactional()
    public List<UserResponse> findAll() {
        List<User> users = repository.findAll();
        return users.stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o ID: " + id));
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