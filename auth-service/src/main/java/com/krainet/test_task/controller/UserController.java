package com.krainet.test_task.controller;

import com.krainet.test_task.dto.user.*;
import com.krainet.test_task.mapper.UserMapper;
import com.krainet.test_task.model.UserEntity;
import com.krainet.test_task.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @PostMapping()
    public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                                   UriComponentsBuilder uriComponentsBuilder
    ) {
        UserResponseDto userResponseDto = userMapper.toDto(userService.addUser(userRequestDto));
        return ResponseEntity
                .created(uriComponentsBuilder
                        .pathSegment("{userId}")
                        .build(Map.of("userId",userResponseDto.getId())))
                .body(userResponseDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable(name = "userId") Long userId) {
        UserResponseDto userResponseDto = userMapper.toDto(userService.getUserById(userId));
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        UserFilter filter = new UserFilter(username,email);
        Pageable pageable = PageRequest.of(page,size, pageSort(sort));
        List<UserResponseDto> users = userMapper.toDtos(userService.getAllUsers(filter, pageable));

        return ResponseEntity.ok(users);
    }

    private Sort pageSort(String[] sort) {
        if(sort.length>=2){
            String property = sort[0];
            String direction = sort[1];

            return Sort.by(Sort.Direction.fromString(direction), property);
        }

        return Sort.unsorted();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable(name = "userId") Long userId, Locale locale) {
        userService.deleteById(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(messageSource.getMessage("message.user.successfully_delete",new Object[]{userId},locale));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable(name = "userId") Long userId,
                                                 @Valid @RequestBody UserUpdateForAdminDto userUpdateDto) {
        UserEntity userEntity = userService.updateUserForAdmin(userId,userUpdateDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userMapper.toDto(userEntity));
    }
}
