package com.company.backend.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions de l'application.
 * <p>
 * Intercepte toutes les exceptions et retourne des réponses JSON
 * uniformes avec les codes HTTP appropriés.
 * </p>
 *
 * @author Fethi Benseddik
 * @version 1.0
 * @since 2025
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions ResourceNotFoundException (404).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request
    ) {
        log.debug("Resource not found: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Gère les exceptions BadRequestException (400).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex,
            WebRequest request
    ) {
        log.debug("Bad request: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Gère les exceptions AuthenticationException (401).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex,
            WebRequest request
    ) {
        log.debug("Authentication error: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Gère les exceptions AccountLockedException (423).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ErrorResponse> handleAccountLockedException(
            AccountLockedException ex,
            WebRequest request
    ) {
        log.warn("Account locked: {}", ex.getMessage());

        Map<String, Object> details = new HashMap<>();
        details.put("lockedUntil", ex.getLockedUntil().toString());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.LOCKED.value(),
                "Locked",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""),
                details
        );

        return ResponseEntity.status(HttpStatus.LOCKED).body(response);
    }

    /**
     * Gère les exceptions AccessDeniedException (403).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            WebRequest request
    ) {
        log.debug("Access denied: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Forbidden",
                "Vous n'avez pas les permissions nécessaires",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Gère les erreurs de validation des arguments (400).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        log.debug("Validation error: {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Erreur de validation des données",
                request.getDescription(false).replace("uri=", ""),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Gère les violations de contraintes (400).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        log.debug("Constraint violation: {}", ex.getMessage());

        Map<String, Object> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Erreur de validation des contraintes",
                request.getDescription(false).replace("uri=", ""),
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Gère toutes les autres exceptions (500).
     *
     * @param ex      l'exception
     * @param request la requête web
     * @return la réponse d'erreur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex,
            WebRequest request
    ) {
        log.error("Erreur serveur inattendue", ex);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Une erreur inattendue s'est produite",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Structure standard d'une réponse d'erreur.
     *
     * @param status    le code HTTP
     * @param error     le type d'erreur
     * @param message   le message d'erreur
     * @param path      le chemin de la requête
     * @param timestamp l'horodatage de l'erreur
     * @param details   les détails additionnels
     */
    public record ErrorResponse(
            int status,
            String error,
            String message,
            String path,
            Instant timestamp,
            Map<String, Object> details
    ) {
        public ErrorResponse(int status, String error, String message, String path) {
            this(status, error, message, path, Instant.now(), null);
        }

        public ErrorResponse(int status, String error, String message, String path, Map<String, Object> details) {
            this(status, error, message, path, Instant.now(), details);
        }
    }
}