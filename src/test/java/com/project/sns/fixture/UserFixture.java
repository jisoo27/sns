package com.project.sns.fixture;

import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.domain.User;

public class UserFixture {

    public static User get(UserJoinRequest userJoinRequest) {
        return User.builder()
                .id(1L)
                .age(userJoinRequest.getAge())
                .email(userJoinRequest.getEmail())
                .password(userJoinRequest.getPassword())
                .userName(userJoinRequest.getUserName())
                .nickName(userJoinRequest.getNickName())
                .address(userJoinRequest.getAddress())
                .profileMessage(userJoinRequest.getProfileMessage())
                .profileImage(userJoinRequest.getProfileImage())
                .build();
    }
}
