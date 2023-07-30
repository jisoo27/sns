package com.project.sns.alarm.dto;

import com.project.sns.alarm.domain.Alarm;
import com.project.sns.alarm.domain.AlarmArgs;
import com.project.sns.alarm.domain.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmResponse {

    private Long id;
    private AlarmType alarmType;
    private AlarmArgs args;
    private String text;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static AlarmResponse of(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getAlarmType(),
                alarm.getArgs(),
                alarm.getAlarmType().getAlarmText(),
                alarm.getRegisteredAt(),
                alarm.getUpdatedAt(),
                alarm.getDeletedAt()
        );
    }
}
