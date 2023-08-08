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

    @DisplayName("로그인을 할 수 있다.")
    @Test
    void loginTest() {

        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(베어러_인증_응답에서_token_가져오기(response)).isNotBlank()
        );
    }

    @DisplayName("회원가입을 하지 않은 경우에는 로그인에 실패한다.")
    @Test
    void loginExceptionTest() {

        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @DisplayName("로그인 시 틀린 비밀번호를 입력할 경우 로그인에 실패한다.")
    @Test
    void loginExceptionTest2() {

        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        var response = 베어러_인증_로그인_요청(EMAIL, "wrong_password");

        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("회원 탈퇴를 할 수 있다.")
    @Test
    void withdrawalTest() {

        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        var 유효한_토큰 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var token = 베어러_인증_응답에서_token_가져오기(유효한_토큰);

        var response = 회원_탈퇴_요청(token);

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @DisplayName("회원 탈퇴를 한 경우 로그인에 실패한다.")
    @Test
    void loginExceptionTest3() {

        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");

        var 유효한_토큰 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var token = 베어러_인증_응답에서_token_가져오기(유효한_토큰);
        회원_탈퇴_요청(token);

        var response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

}
