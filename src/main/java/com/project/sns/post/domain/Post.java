package com.project.sns.post.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
