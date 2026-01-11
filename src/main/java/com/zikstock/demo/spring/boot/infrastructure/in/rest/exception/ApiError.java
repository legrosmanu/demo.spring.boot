package com.zikstock.demo.spring.boot.infrastructure.in.rest.exception;

import java.time.OffsetDateTime;

public record ApiError(OffsetDateTime timestamp, int status, String error, String message) {
}
