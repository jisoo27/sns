package com.project.sns.acceptance.user;

import com.project.sns.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class UserAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    void before() {
        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");
    }

    @Test
    @DisplayName("유효한 토큰을 가지고 나의 정보를 조회할 수 있다.")
    void getMyInfoTest() {

        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var token = 베어러_인증_응답에서_token_가져오기(베어러_인증_로그인_응답);
        var response = 토큰_인증으로_내_회원정보_조회_요청(token);
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    @DisplayName("유효한 토큰이 아닐 경우 나의 정보를 조회하는데 실패한다.")
    void getMyInfoExceptionTest() {
        String wrong_token = "/";
        var response = 토큰_인증으로_내_회원정보_조회_요청(wrong_token);
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("유효한 토큰일 경우 나의 정보를 수정할 수 있다.")
    void editMyInfoTest() {
        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var token = 베어러_인증_응답에서_token_가져오기(베어러_인증_로그인_응답);
        var response = 토큰_인증으로_내_회원정보_수정_요청(token, "안녕안녕", "//", "비나사나");

        var getResponse = 토큰_인증으로_내_회원정보_조회_요청(token);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(getResponse.jsonPath().getString("profileMessage")).isEqualTo("안녕안녕"),
                () -> assertThat(getResponse.jsonPath().getString("profileImage")).isEqualTo("//"),
                () -> assertThat(getResponse.jsonPath().getString("nickName")).isEqualTo("비나사나")
        );
    }

    @Test
    @DisplayName("유효하지 않은 토큰일 경우 나의 정보를 수정하는데 실패한다.")
    void editMyInfoExceptionTest() {

        String wrong_token = "/";
        var response = 토큰_인증으로_내_회원정보_수정_요청(wrong_token, "안녕안녕", "//", "비나사나");

        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
