package com.krainet.test_task.service;


import com.krainet.test_task.dto.auth.AuthRequestDto;
import com.krainet.test_task.dto.auth.AuthResponseDto;
import com.krainet.test_task.dto.auth.RegisterRequestDto;
import com.krainet.test_task.model.UserEntity;

public interface AuthService {
    UserEntity registerUser(RegisterRequestDto registerRequestDto);
    AuthResponseDto authenticateUser(AuthRequestDto authRequestDto);
}

