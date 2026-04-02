package com.ryan.users.controller.handler;

import com.ryan.users.controller.handler.exception.CpfAlreadyExistentException;
import com.ryan.users.controller.handler.exception.EmailAlreadyExistentException;
import com.ryan.users.controller.handler.exception.UnderAgeException;
import com.ryan.users.controller.handler.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistentException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistent(EmailAlreadyExistentException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT.name(),
            ex.getMessage(),
            LocalDateTime.now(),
            request.getRequestURI(),
            null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CpfAlreadyExistentException.class)
    public ResponseEntity<ErrorResponse> handleCpfAlreadyExistent(CpfAlreadyExistentException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.name(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnderAgeException.class)
    public ResponseEntity<ErrorResponse> handleUnderAge(UnderAgeException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String path = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String paramName = ex.getName();
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String message = String.format("Parâmetro '%s' com valor '%s' não pode ser convertido para %s.", paramName, value, requiredType);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                message,
                LocalDateTime.now(),
                path,
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request) {
        String path = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String propertyName = ex.getPropertyName() != null ? ex.getPropertyName() : "unknown";
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String value = ex.getValue() != null ? ex.getValue().toString() : "null";
        String message = String.format("Valor '%s' para '%s' não pode ser convertido para %s.", value, propertyName, requiredType);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                message,
                LocalDateTime.now(),
                path,
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String path = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        String errorMessage = "Formato de dado inválido na requisição.";

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errorMessage,
                LocalDateTime.now(),
                path,
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String path = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                "Erro de validação",
                LocalDateTime.now(),
                path,
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        String path = request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Um erro inesperado aconteceu",
                LocalDateTime.now(),
                path,
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}