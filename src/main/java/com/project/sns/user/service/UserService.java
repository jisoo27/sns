package com.project.sns.user.service;

import com.project.sns.configuration.JwtTokenUtils;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.controller.dto.response.UserResponse;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static com.project.sns.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public UserJoinResponse join(UserJoinRequest userJoinRequest) {

        userRepository.findByEmail(userJoinRequest.getEmail()).ifPresent(it -> {
            throw new SnsApplicationException(DUPLICATED_USER_EMAIL);
        });
        User user = userRepository.save(User.of(userJoinRequest.getAge(),
                userJoinRequest.getEmail(), encoder.encode(userJoinRequest.getPassword()),userJoinRequest.getUserName(),
                userJoinRequest.getNickName(), userJoinRequest.getAddress(), userJoinRequest.getProfileMessage(),
                userJoinRequest.getProfileImage()));
        return UserJoinResponse.of(user);
    }

    public UserLoginResponse login(String email, String password) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(INVALID_PASSWORD);
        }
        String token = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);
        return UserLoginResponse.of(token);

    }

    public UserResponse getMyInformation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
        return UserResponse.of(user);
    }

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
    }
}
