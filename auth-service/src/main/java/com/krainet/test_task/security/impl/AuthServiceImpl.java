package com.krainet.test_task.security.impl;

import com.krainet.test_task.dto.auth.AuthRequestDto;
import com.krainet.test_task.dto.auth.AuthResponseDto;
import com.krainet.test_task.dto.auth.RegisterRequestDto;
import com.krainet.test_task.dto.user.UserRequestDto;
import com.krainet.test_task.dto.user.UserResponseDto;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.security.AuthService;
import com.krainet.test_task.security.SecurityService;
import com.krainet.test_task.security.TokenDetails;
import com.krainet.test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(RegisterRequestDto registerRequestDto) {
        return userService.addUser(UserRequestDto.builder()
                .email(registerRequestDto.getEmail())
                .role("USER")
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .build());
    }

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        TokenDetails tokenDetails = securityService.authenticate(authRequestDto.getUsername(), authRequestDto.getPassword());
        return AuthResponseDto.builder()
                .userId(tokenDetails.getId())
                .token(tokenDetails.getToken())
                .expiresAt(tokenDetails.getExpiredAt())
                .issuedAt(tokenDetails.getIssuedAt())
                .role(tokenDetails.getRole())
                .build();
    }
}
