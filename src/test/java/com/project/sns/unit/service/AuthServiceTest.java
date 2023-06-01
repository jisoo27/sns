package com.project.sns.unit.service;

import com.project.sns.user.repository.UserRepository;
import com.project.sns.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


}
