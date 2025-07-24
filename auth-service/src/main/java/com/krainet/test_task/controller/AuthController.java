package com.krainet.test_task.controller;

import com.krainet.test_task.dto.auth.AuthRequestDto;
import com.krainet.test_task.dto.auth.AuthResponseDto;
import com.krainet.test_task.dto.auth.RegisterRequestDto;
import com.krainet.test_task.dto.user.UserResponseDto;
import com.krainet.test_task.mapper.UserMapper;
import com.krainet.test_task.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody RegisterRequestDto registerRequestDto){
        return userMapper.toDto(authService.registerUser(registerRequestDto));
    }

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto authRequestDto){
        return authService.authenticateUser(authRequestDto);
    }

}
