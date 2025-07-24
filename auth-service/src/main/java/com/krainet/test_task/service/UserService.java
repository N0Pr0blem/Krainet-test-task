package com.krainet.test_task.service;

import com.krainet.test_task.dto.user.UserFilter;
import com.krainet.test_task.dto.user.UserRequestDto;
import com.krainet.test_task.dto.user.UserUpdateDto;
import com.krainet.test_task.dto.user.UserUpdateForAdminDto;
import com.krainet.test_task.model.UserEntity;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserEntity getUserById(Long userId);

    List<UserEntity> getAllUsers();

    UserEntity addUser(UserRequestDto userRequestDto);

    void deleteById(Long userId);

    UserEntity getUserByUsername(String name);

    List<UserEntity> getAllUsers(UserFilter filter, Pageable pageable);

    UserEntity updateUser(Long userId, UserUpdateDto userUpdateDto);

    UserEntity updateUser(UserEntity user, UserUpdateDto userUpdateDto);

    void deleteByUsername(String name);

    UserEntity updateUserForAdmin(Long userId, UserUpdateForAdminDto userUpdateDto);
}
