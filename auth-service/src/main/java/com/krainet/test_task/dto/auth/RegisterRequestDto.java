package com.krainet.test_task.dto.auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDto {
    @NotBlank(message = "{error.user.dto.valid.username.not_blank}")
    @Size(max=64, min=2, message = "{error.user.dto.valid.username.size}")
    private String username;

    @NotBlank(message = "{error.user.dto.valid.password.not_blank}")
    @Size(max=256, min=8, message = "{error.user.dto.valid.password.size}")
    private String password;

    @NotBlank(message = "{error.user.dto.valid.email.not_blank}")
    @Email(message = "{error.user.dto.valid.email.not_email}")
    private String email;

    @NotBlank(message = "{error.user.dto.valid.first_name.not_blank}")
    private String firstName;

    @NotBlank(message = "{error.user.dto.valid.last_name.not_blank}")
    private String lastName;
}
