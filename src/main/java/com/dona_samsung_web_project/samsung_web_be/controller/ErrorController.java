package com.dona_samsung_web_project.samsung_web_be.controller;

import com.dona_samsung_web_project.samsung_web_be.exception.*;
import com.dona_samsung_web_project.samsung_web_be.model.response.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(RestTemplateException.class)
    ResponseEntity<ErrorResponse> restTemplateException(RestTemplateException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("X07", exception.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("X07", exception.getMessage()));
    }

    @ExceptionHandler(EntityExistException.class)
    ResponseEntity<ErrorResponse> handleEntityExistViolationException(EntityExistException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("X03", exception.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception, HttpServletRequest request) {
        Set<ConstraintViolation<?>> violation = exception.getConstraintViolations();
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> error : violation) {
            errors.add(error.getMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("X02", errors.toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpServletRequest request
    ) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            errors.add(error.getDefaultMessage());
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("X04", errors.toString()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(
            NotFoundException exception, HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("X01", exception.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException exception, HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("X05", exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(
            Exception exception, HttpServletRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("X06", exception.getMessage()));
    }
}
