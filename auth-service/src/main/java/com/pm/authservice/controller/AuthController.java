package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDto;
import com.pm.authservice.dto.LoginResponseDto;
import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Tag(name="Auth", description = "API for Auth management")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Optional<String> token = authService.authenticate(loginRequestDto);
        if (token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(new LoginResponseDto(token.get()));
    }

    @Operation(summary = "Validate Token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader) {

        //Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                :ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
