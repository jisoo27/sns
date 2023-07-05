package com.project.sns.like.repository;

import com.project.sns.like.domain.Like;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    Page<Like> findAllByUser(User user, Pageable pageable);
}
