package com.project.sns.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @Column(name = "register_at")
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "delete_at")
    private LocalDateTime deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    void updatedAt() {
        this.updateAt = LocalDateTime.now();
    }

}
