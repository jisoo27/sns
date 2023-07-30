package com.project.sns.alarm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AlarmArgs {

    private Long fromUserId;
    private Long targetId;
}
