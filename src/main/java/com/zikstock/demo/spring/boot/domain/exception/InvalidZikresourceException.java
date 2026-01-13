package com.zikstock.demo.spring.boot.domain.exception;

public class InvalidZikresourceException extends RuntimeException {
    public InvalidZikresourceException(String message) {
        super(message);
    }
}
