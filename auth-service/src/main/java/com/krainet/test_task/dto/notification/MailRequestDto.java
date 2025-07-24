package com.krainet.test_task.dto.notification;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.krainet.test_task.dto.user.UserMailDto;
import com.krainet.test_task.model.UserChangeType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class MailRequestDto {
    private List<String> mails;
    private UserMailDto user;
    private UserChangeType changeType;
}
