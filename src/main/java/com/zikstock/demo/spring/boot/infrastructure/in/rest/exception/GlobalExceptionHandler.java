package com.zikstock.demo.spring.boot.infrastructure.in.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.zikstock.demo.spring.boot.domain.exception.ZikresourceNotFound;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpHeaders headers,
                        HttpStatusCode status,
                        WebRequest request) {
                String message = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

                ApiError apiError = new ApiError(
                                OffsetDateTime.now(),
                                status.value(),
                                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                                message);
                return handleExceptionInternal(ex, apiError, headers, status, request);
        }

        @Override
        protected ResponseEntity<Object> handleHttpMessageNotReadable(
                        HttpMessageNotReadableException ex, HttpHeaders headers,
                        HttpStatusCode status, WebRequest request) {
                ApiError apiError = new ApiError(
                                OffsetDateTime.now(),
                                status.value(),
                                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                                ex.getMessage());
                return handleExceptionInternal(ex, apiError, headers, status, request);
        }

        @ExceptionHandler(ZikresourceNotFound.class)
        public ResponseEntity<Object> handleZikresourceNotFound(ZikresourceNotFound ex, WebRequest request) {
                ApiError apiError = new ApiError(
                                OffsetDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                HttpStatus.NOT_FOUND.getReasonPhrase(),
                                ex.getMessage());
                return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
                ApiError apiError = new ApiError(
                                OffsetDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                                ex.getMessage());
                return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
}
