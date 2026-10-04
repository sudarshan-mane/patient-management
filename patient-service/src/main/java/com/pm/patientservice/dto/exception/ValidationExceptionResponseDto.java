package com.pm.patientservice.dto.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationExceptionResponseDto (
     LocalDateTime timestamp,
     int statusCode,
     String error,
     String message,
     String path,
     Map<String, String> fieldErrors

){}
