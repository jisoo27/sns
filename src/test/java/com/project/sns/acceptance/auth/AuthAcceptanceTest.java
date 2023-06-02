package com.project.sns.acceptance.auth;

import com.project.sns.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";


    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void joinTest() {

        var response = 회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value())
        );
    }

    @DisplayName("이미 회원가입이 되어있는 이메일일 경우 회원가입에 실패한다.")
    @Test
    void joinExceptionTest() {

        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        var response = 회원가입_요청(20, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

}
