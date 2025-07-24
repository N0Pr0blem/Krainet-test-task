package com.krainet.test_task.service.impl;

import com.krainet.test_task.dto.auth.AuthRequestDto;
import com.krainet.test_task.dto.auth.AuthResponseDto;
import com.krainet.test_task.dto.auth.RegisterRequestDto;
import com.krainet.test_task.dto.user.UserRequestDto;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.security.TokenDetails;
import com.krainet.test_task.service.AuthService;
import com.krainet.test_task.service.SecurityService;
import com.krainet.test_task.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final SecurityService securityService;

    private Logger logger = LogManager.getLogger(AuthServiceImpl.class);

    @Override
    public UserEntity registerUser(RegisterRequestDto registerRequestDto) {
        UserRequestDto registerUser = UserRequestDto.builder()
                .email(registerRequestDto.getEmail())
                .role("USER")
                .username(registerRequestDto.getUsername())
                .password(registerRequestDto.getPassword())
                .firstName(registerRequestDto.getFirstName())
                .lastName(registerRequestDto.getLastName())
                .build();

        logger.info("Request to register user");

        return userService.addUser(registerUser);
    }

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequestDto) {
        TokenDetails tokenDetails = securityService.authenticate(authRequestDto.getUsername(), authRequestDto.getPassword());
        logger.info("Request to authentication");
        return AuthResponseDto.builder()
                .userId(tokenDetails.getId())
                .token(tokenDetails.getToken())
                .expiresAt(tokenDetails.getExpiredAt())
                .issuedAt(tokenDetails.getIssuedAt())
                .role(tokenDetails.getRole())
                .build();
    }
}
