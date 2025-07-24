package com.krainet.test_task.service.impl;

import com.krainet.test_task.dto.user.UserFilter;
import com.krainet.test_task.dto.user.UserRequestDto;
import com.krainet.test_task.dto.user.UserUpdateDto;
import com.krainet.test_task.exception.ApiException;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.repository.UserRepository;
import com.krainet.test_task.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("error.user.not_found", userId, "GETTING_USER_EXCEPTION"));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity addUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new ApiException("error.user.username.exists", userRequestDto.getUsername());
        }
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            throw new ApiException("error.user.email.exists", userRequestDto.getEmail());
        }
        return userRepository.save(UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .email(userRequestDto.getEmail())
                .role(userRequestDto.getRole())
                .isActive(true)
                .build());
    }

    @Override
    public void deleteById(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new ApiException("error.user.not_found", userId, "USER_NOT_FOUND");
        }
    }

    @Override
    public UserEntity getUserByUsername(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new ApiException("error.user.username.not_found", name, "NO_SUCH_USER"));
    }

    @Override
    public List<UserEntity> getAllUsers(UserFilter filter, Pageable pageable) {
        Specification<UserEntity> spec = buildSpecification(filter);

        return userRepository.findAll(spec, pageable).stream().toList();
    }

    @Override
    public UserEntity updateUser(Long userId, UserUpdateDto userUpdateDto) {
        UserEntity user = getUserById(userId);

        if (userUpdateDto.getRole() != null && !userUpdateDto.getRole().isEmpty()) {
            user.setRole(userUpdateDto.getRole());
        }
        if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
            user.setPassword(userUpdateDto.getPassword());
        }

        return userRepository.save(user);
    }

    private Specification<UserEntity> buildSpecification(UserFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getEmail() != null) {
                predicates.add(cb.equal(root.get("email"), filter.getEmail()));
            }
            if (filter.getUsername() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("username"), filter.getUsername()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
