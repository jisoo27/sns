package com.project.sns.user.controller;

import com.project.sns.user.controller.dto.request.UserEditInfoRequest;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.request.UserLoginRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.controller.dto.response.UserResponse;
import com.project.sns.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        // TODO: 컨트롤러에서 받는 요청 dto 를 그대로 service layer 에 보내지 말것.
        UserJoinResponse response = userService.join(request);
        return ResponseEntity.created(URI.create("/users/" + response.getId())).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        UserLoginResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<UserResponse> getMyInformation(Authentication authentication) {
        UserResponse userResponse = userService.getMyInformation(authentication.getName());
        return ResponseEntity.ok().body(userResponse);
    }

    @PatchMapping("/my")
    public ResponseEntity<UserResponse> editMyInformation(Authentication authentication, @RequestBody UserEditInfoRequest request) {
        UserResponse userResponse = userService.editMyInformation(authentication.getName(), request);
        return ResponseEntity.ok().body(userResponse);
    }
}
