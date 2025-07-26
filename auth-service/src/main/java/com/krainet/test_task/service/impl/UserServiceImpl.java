package com.krainet.test_task.service.impl;

import com.krainet.test_task.dto.user.*;
import com.krainet.test_task.exception.ApiException;
import com.krainet.test_task.model.UserChangeType;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.repository.UserRepository;
import com.krainet.test_task.service.MailService;
import com.krainet.test_task.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    private Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public UserEntity getUserById(Long userId) {
        logger.info("Get user with ID: " + userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("error.user.not_found", userId, "GETTING_USER_EXCEPTION"));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        logger.info("Get all users ");
        return userRepository.findAll();
    }

    @Override
    public UserEntity addUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            logger.warn("User with username: " + userRequestDto.getUsername() + " is present");
            throw new ApiException("error.user.username.exists", userRequestDto.getUsername());
        }
        if (userRepository.findByEmail(userRequestDto.getEmail()).isPresent()) {
            logger.warn("User with email: " + userRequestDto.getEmail() + " is present");
            throw new ApiException("error.user.email.exists", userRequestDto.getEmail());
        }

        UserEntity user = UserEntity.builder()
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .firstName(userRequestDto.getFirstName())
                .lastName(userRequestDto.getLastName())
                .email(userRequestDto.getEmail())
                .role(userRequestDto.getRole())
                .isActive(true)
                .build();

        sendMail(user, UserChangeType.CREATED);
        logger.info("Add new user: " + user.toString());

        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        UserEntity user = getUserById(userId);

        sendMail(user, UserChangeType.DELETED);
        userRepository.delete(user);
        logger.info("User with ID: " + userId + " was deleted");
    }

    @Override
    public UserEntity getUserByUsername(String name) {
        logger.info("Try get user with username: " + name);

        return userRepository.findByUsername(name)
                .orElseThrow(() -> new ApiException("error.user.username.not_found", name, "NO_SUCH_USER"));
    }

    @Override
    public List<UserEntity> getAllUsers(UserFilter filter, Pageable pageable) {
        Specification<UserEntity> spec = buildSpecification(filter);
        logger.info("Get all users with specification: " + spec.toString());

        return userRepository.findAll(spec, pageable).stream().toList();
    }

    @Override
    public UserEntity updateUser(Long userId, UserUpdateDto userUpdateDto) {
        UserEntity user = getUserById(userId);

        return updateUser(user, userUpdateDto);
    }

    @Override
    public UserEntity updateUser(UserEntity user, UserUpdateDto userUpdateDto) {

        if (userRepository.findByUsername(userUpdateDto.getUsername()).isEmpty()
                && userRepository.findByEmail(userUpdateDto.getEmail()).isEmpty()
        ) {
            if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isEmpty()) {
                user.setUsername(userUpdateDto.getUsername());
            }
            if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isEmpty()) {
                user.setEmail(userUpdateDto.getEmail());
            }
            if (userUpdateDto.getFirstName() != null && !userUpdateDto.getFirstName().isEmpty()) {
                user.setFirstName(userUpdateDto.getFirstName());
            }
            if (userUpdateDto.getLastName() != null && !userUpdateDto.getLastName().isEmpty()) {
                user.setLastName(userUpdateDto.getLastName());
            }
            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            }
            sendMail(user, UserChangeType.UPDATED);
        }
        logger.info("User with ID: " + user.getId() + " updated");

        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String name) {
        UserEntity user = getUserByUsername(name);

        sendMail(user, UserChangeType.DELETED);
        userRepository.delete(user);
        logger.info("Delete user with username: " + name);
    }

    @Override
    public UserEntity updateUserForAdmin(Long userId, UserUpdateForAdminDto userUpdateDto) {
        UserEntity user = getUserById(userId);

        if (userRepository.findByUsername(userUpdateDto.getUsername()).isEmpty()
                && userRepository.findByEmail(userUpdateDto.getEmail()).isEmpty()
        ) {
            if (userUpdateDto.getUsername() != null && !userUpdateDto.getUsername().isEmpty()) {
                user.setUsername(userUpdateDto.getUsername());
            }
            if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isEmpty()) {
                user.setEmail(userUpdateDto.getEmail());
            }
            if (userUpdateDto.getFirstName() != null && !userUpdateDto.getFirstName().isEmpty()) {
                user.setFirstName(userUpdateDto.getFirstName());
            }
            if (userUpdateDto.getLastName() != null && !userUpdateDto.getLastName().isEmpty()) {
                user.setLastName(userUpdateDto.getLastName());
            }
            if (userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
            }
            if (userUpdateDto.getRole() != null && !userUpdateDto.getRole().isEmpty()) {
                user.setRole(userUpdateDto.getRole());
            }

            sendMail(user, UserChangeType.UPDATED);
        }
        logger.info("User with ID: " + user.getId() + " updated by Admin");

        return userRepository.save(user);
    }

    @Override
    public List<String> getAdminsEmail() {
        return userRepository.getAdminsEmail();
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

    private void sendMail(UserEntity user, UserChangeType userChangeType) {
        if (user.getRole().equals("USER")) {
            mailService.sendMails(userRepository.getAdminsEmail(),
                    UserMailDto.builder()
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .build(),
                    userChangeType);
        }
    }
}
