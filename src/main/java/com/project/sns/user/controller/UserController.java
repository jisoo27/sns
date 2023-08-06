package com.project.sns.user.controller;

import com.project.sns.exception.ErrorCode;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.user.controller.dto.response.AlarmResponse;
import com.project.sns.user.controller.dto.request.UserEditInfoRequest;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.request.UserLoginRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.controller.dto.response.UserResponse;
import com.project.sns.user.domain.User;
import com.project.sns.user.service.UserService;
import com.project.sns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/alarm")
    public ResponseEntity<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(() -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR));
        Page<AlarmResponse> response = userService.alarmList(user.getId(), pageable).map(AlarmResponse::of);
        return ResponseEntity.ok().body(response);
    }
}
