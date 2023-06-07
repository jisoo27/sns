package com.project.sns.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEditInfoRequest {

    private String profileMessage;

    private String profileImage;

    private String nickName;
}
