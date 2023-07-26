package com.project.sns.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserLoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
