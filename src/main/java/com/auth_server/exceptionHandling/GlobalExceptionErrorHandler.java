package com.auth_server.exceptionHandling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionErrorHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", "Request url is not found");
        error.put("path", ex.getRequestURL());
        error.put("statusCode", ex.getStatusCode());
        error.put("status", false);
        logger.error("Error from global NoHandlerFoundException " + ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, Object> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 400);
        response.put("status", false);
        response.put("message", "Validation failed");
        response.put("requiredFields", fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", "Request url is not found");
        error.put("notFoundMethod", ex.getMethod());
        error.put("statusCode", ex.getStatusCode());
        error.put("status", false);
        logger.error("Error from global HttpRequestMethodNotSupportedException " + ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> IllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("errorMessage", ex.getMessage());
        error.put("statusCode", 404);
        error.put("status", false);
        error.put("date", null);
        logger.error("Error from global IllegalArgumentException " + ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustom(CustomException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", ex.getCode());
        response.put("status", false);
        response.put("message", ex.getMessage());
        response.put("data", null);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 500);
        response.put("status", false);
        response.put("message", ex.getMessage());
        response.put("data", null);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
