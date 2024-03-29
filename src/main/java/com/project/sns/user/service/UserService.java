package com.project.sns.user.service;

import com.project.sns.user.domain.Alarm;
import com.project.sns.user.repository.AlarmRepository;
import com.project.sns.user.repository.UserCacheRepository;
import com.project.sns.util.JwtTokenUtils;
import com.project.sns.exception.SnsApplicationException;
import com.project.sns.user.controller.dto.request.UserEditInfoRequest;
import com.project.sns.user.controller.dto.request.UserJoinRequest;
import com.project.sns.user.controller.dto.response.UserJoinResponse;
import com.project.sns.user.controller.dto.response.UserLoginResponse;
import com.project.sns.user.controller.dto.response.UserResponse;
import com.project.sns.user.domain.User;
import com.project.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.project.sns.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Transactional
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
        User user = loadUserByEmail(email);
        userCacheRepository.setUser(user);

        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(INVALID_PASSWORD);
        }
        String token = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);
        return UserLoginResponse.of(token);

    }

    public UserResponse getMyInformation(String email) {
        User user = getUserOrException(email);
        return UserResponse.of(user);
    }

    public User loadUserByEmail(String email) {
        return userCacheRepository.getUser(email).orElseGet(() -> getUserOrException(email));
    }

    @Transactional
    public UserResponse editMyInformation(String email, UserEditInfoRequest request) {
        User user = getUserOrException(email);
        user.editInfo(request.getProfileMessage(), request.getProfileImage(), request.getNickName());
        return UserResponse.of(user);
    }

    public Page<Alarm> getAlarmList(Long userId, Pageable pageable) {
        return alarmRepository.findAllByUserId(userId, pageable);
    }

    private User getUserOrException(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new SnsApplicationException(USER_NOT_FOUND));
    }

    @Transactional
    public void delete(String email) {
        User user = getUserOrException(email);
        userRepository.delete(user);
        userCacheRepository.deleteUser(user);
    }
}
