package com.project.sns.user.controller.dto.response;

import com.project.sns.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private String profileMessage;

    private String profileImage;

    private String nickName;

    public static UserResponse of(User user) {
        return new UserResponse(user.getProfileMessage(), user.getProfileImage(), user.getNickName());
    }
}
