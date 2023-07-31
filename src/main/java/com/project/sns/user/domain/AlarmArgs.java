package com.project.sns.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmArgs {

    private Long fromUserId;
    private Long targetId;
}
