package com.project.sns.image.repository;

import com.project.sns.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByPostId(Long postId);
}
