package com.project.sns.user.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private String token;

    public static UserLoginResponse of(String token) {
        return new UserLoginResponse(token);
    }
}
