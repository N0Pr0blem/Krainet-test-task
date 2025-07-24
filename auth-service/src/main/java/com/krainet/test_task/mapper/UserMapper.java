package com.krainet.test_task.mapper;

import com.krainet.test_task.dto.user.UserResponseDto;
import com.krainet.test_task.mapper.base.Mappable;
import com.krainet.test_task.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<UserEntity, UserResponseDto> {
}
