package com.project.sns.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class UserJoinRequest {

    @NotBlank
    private int age;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Max(20)
    private String userName;

    @NotBlank
    private String nickName;

    @NotBlank
    private String address;

    @NotBlank
    private String profileMessage;

    private String profileImage;

}
