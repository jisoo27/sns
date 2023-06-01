package com.project.sns.like.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
