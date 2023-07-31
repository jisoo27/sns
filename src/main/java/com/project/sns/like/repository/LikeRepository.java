package com.project.sns.like.repository;

import com.project.sns.like.domain.Like;
import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndPost(User user, Post post);

    Page<Like> findAllByUser(User user, Pageable pageable);

    long countByPost(Post post);

    @Transactional
    @Modifying
    @Query("UPDATE likes l SET deleted_at = NOW() where l.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}
