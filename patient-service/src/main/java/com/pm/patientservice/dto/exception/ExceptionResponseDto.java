package com.pm.patientservice.dto.exception;
import java.time.LocalDateTime;

public record ExceptionResponseDto (
    LocalDateTime timestamp,
    int statusCode,
    String error,
    String message,
    String path
){ }