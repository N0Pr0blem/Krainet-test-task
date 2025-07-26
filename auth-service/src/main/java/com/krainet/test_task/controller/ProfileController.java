package com.krainet.test_task.controller;

import com.krainet.test_task.dto.user.UserResponseDto;
import com.krainet.test_task.dto.user.UserUpdateDto;
import com.krainet.test_task.mapper.UserMapper;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<UserResponseDto> getProfile(Principal principal){
        UserEntity user = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProfile(Principal principal, Locale locale){
        userService.deleteByUsername(principal.getName());
        return ResponseEntity.ok(
                messageSource.getMessage("message.user.successfully_delete",new Object[]{principal.getName()},locale)
        );
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateProfile(@Valid @RequestBody UserUpdateDto userUpdateDto, Principal principal){
        UserEntity user = userService.getUserByUsername(principal.getName());
        return ResponseEntity.ok(
                userMapper.toDto(userService.updateUser(user, userUpdateDto))
        );
    }

}
