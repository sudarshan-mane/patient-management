package com.pm.authservice.service;

import com.pm.authservice.JwtUtil;
import com.pm.authservice.dto.LoginRequestDto;
import com.pm.authservice.model.User;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService,  PasswordEncoder passwordEncoder,  JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDto loginRequestDto) {

        //Get user from DB, match passwords, generate token
        Optional<String> token = userService
                .findByEmail(loginRequestDto.getEmail()) //find user
                .filter(u -> passwordEncoder.matches(loginRequestDto.getPassword(), u.getPassword())) //check if password match , if don't match return empty.
                .map(u-> jwtUtil.generateToken(u.getEmail(), u.getRole()));// if password match, return token.

         return token;
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch(JwtException e){
            return false;
        }
    }
}
