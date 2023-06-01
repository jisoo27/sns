package com.project.sns.user.service;

import com.project.sns.exception.SnsApplicationException;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.project.sns.exception.ErrorCode.DUPLICATED_USER_EMAIL;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserJoinResponse join(UserJoinRequest userJoinRequest) {

        // 가입된 이메일인지 체크해야한다.
        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(it -> {
            throw new SnsApplicationException(DUPLICATED_USER_EMAIL);
        });
        User user = userRepository.save(User.of(userJoinRequest));
        return UserJoinResponse.of(user);
    }

    public UserLoginResponse login() {
        return null;
    }
}
