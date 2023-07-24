package com.project.sns.post.repository;

import com.project.sns.post.domain.Post;
import com.project.sns.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    @Query("SELECT p FROM post p where p.user.age > :low and p.user.age <:high")
    Page<Post> findAllByAge(Pageable pageable, @Param("low") int low, @Param("high") int high);
}
