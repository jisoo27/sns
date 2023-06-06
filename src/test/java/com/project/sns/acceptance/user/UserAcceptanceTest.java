package com.project.sns.acceptance.user;

import com.project.sns.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

class UserAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    void before() {
        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");
    }

    @Test
    @DisplayName("유효한 토큰을 가지고 나의 정보를 조회할 수 있다.")
    void getMyInfo() {

        var 베어러_인증_로그인_응답 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var token = 베어러_인증_응답에서_token_가져오기(베어러_인증_로그인_응답);
        var response = 토큰_인증으로_내_회원정보_조회_요청(token);
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

}
