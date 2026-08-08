package com.hyuse98.scheduler.core.infrastructure.api.advice;

import com.hyuse98.scheduler.core.application.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.hyuse98.scheduler.core.infrastructure.api")
public class CoreExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CoreExceptionHandler.class);

    // Agrupa todas as exceções de "Não Encontrado" (HTTP 404)
    @ExceptionHandler({
            ClientNotFoundException.class,
            ServiceProviderNotFoundException.class,
            ScheduleNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex) {
        LOG.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Agrupa todas as exceções de "Conflito / Já Existe" (HTTP 409 ou 400)
    @ExceptionHandler({
            ClientAlreadyExistException.class,
            ServiceProviderAlreadyExistException.class
    })
    public ResponseEntity<ErrorResponse> handleAlreadyExistExceptions(RuntimeException ex) {
        LOG.warn("Resource already exists: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ClientCollectionEmpty.class)
    public ResponseEntity<ErrorResponse> handleClientCollectionEmpty(ClientCollectionEmpty ex) {
        LOG.warn(ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Captura as validações do domínio (ex: agendamento no passado)
        LOG.warn("Domain validation error: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        LOG.error("Validation Error: {}", message);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error: " + message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        LOG.error("Internal Server Error Occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error Occurred");
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}