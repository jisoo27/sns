package com.project.sns.user.domain;

import com.project.sns.audit.Auditable;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Table(name = "alarm", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@SQLDelete(sql = "UPDATE alarm SET deleted_at = NOW() where id = ?")
@Where(clause = "deleted_at is NULL")
public class Alarm extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private AlarmArgs args;

    public static Alarm of (User user, AlarmType alarmType, AlarmArgs args) {
        Alarm entity = new Alarm();
        entity.setUser(user);
        entity.setAlarmType(alarmType);
        entity.setArgs(args);
        return entity;
    }

    public boolean isCheckUser(User user) {
        return this.getUser() != user;
    }
}
