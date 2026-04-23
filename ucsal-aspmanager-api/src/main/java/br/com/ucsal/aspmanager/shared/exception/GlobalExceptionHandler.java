package br.com.ucsal.aspmanager.shared.exception;

import br.com.ucsal.aspmanager.shared.model.dto.ErroApiResponse;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroApiResponse> authenticationException(AuthenticationException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroApiResponse> accessDeniedException(AccessDeniedException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.FORBIDDEN.value())
                .status(HttpStatus.FORBIDDEN.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErroApiResponse> jwtVerificationException(JWTVerificationException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.UNAUTHORIZED.value())
                .status(HttpStatus.UNAUTHORIZED.name())
                .erros(List.of("Token JWT inválido ou expirado"))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroApiResponse> argumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> listaErro = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ErroApiResponse apiError = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .erros(listaErro)
                .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroApiResponse> messageNotReadableException(HttpMessageNotReadableException ex) {
        String mensagem = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
            .erros(List.of(mensagem))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroApiResponse> illegalArgumentException(IllegalArgumentException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroApiResponse> illegalStateException(IllegalStateException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroApiResponse> entityNotFoundException(EntityNotFoundException ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroApiResponse> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        String mensagem = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.name())
            .erros(List.of(mensagem))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroApiResponse> genericException(Exception ex) {
        ErroApiResponse erro = ErroApiResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .codigo(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .erros(List.of(ex.getMessage()))
                .build();
        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
