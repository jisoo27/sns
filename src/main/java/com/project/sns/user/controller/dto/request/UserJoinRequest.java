package com.project.sns.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserJoinRequest {
    // TODO: 유효성 검증 해야함
    private int age;

    private String email;

    private String password;

    private String userName;

    private String nickName;

    private String address;

    private String profileMessage;

    private String profileImage;

}
