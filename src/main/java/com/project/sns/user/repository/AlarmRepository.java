package com.project.sns.user.repository;

import com.project.sns.user.domain.Alarm;
import com.project.sns.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    Page<Alarm> findAllByUser(User user, Pageable pageable);
}
