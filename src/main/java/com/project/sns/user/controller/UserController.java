package com.project.sns.user.controller;

import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserJoinResponse response = userService.join(userJoinRequest);
        return ResponseEntity.created(URI.create("/users/" + response.getId())).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login() {
        UserLoginResponse response = userService.login();
        return ResponseEntity.ok().body(response);
    }
}
