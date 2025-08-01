package com.krainet.test_task.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.krainet.test_task.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
public class UserRequestDto {
    @NotBlank(message = "{error.user.dto.valid.username.not_blank}")
    @Size(max=64, min=2, message = "{error.user.dto.valid.username.size}")
    private String username;

    @NotBlank(message = "{error.user.dto.valid.password.not_blank}")
    @Size(max=256, min=8, message = "{error.user.dto.valid.password.size}")
    private String password;

    @NotBlank(message = "{error.user.dto.valid.email.not_blank}")
    @Email(message = "{error.user.dto.valid.email.not_email}")
    private String email;

    @NotBlank(message = "{error.user.dto.valid.role.not_blank}")
    private Role role;

    @NotBlank(message = "{error.user.dto.valid.first_name.not_blank}")
    private String firstName;

    @NotBlank(message = "{error.user.dto.valid.last_name.not_blank}")
    private String lastName;
}
