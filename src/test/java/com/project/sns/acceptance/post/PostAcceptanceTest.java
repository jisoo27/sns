package com.project.sns.acceptance.post;

import com.project.sns.acceptance.AcceptanceTest;
import com.project.sns.image.dto.ImageRequest;
import com.project.sns.post.controller.dto.request.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static com.project.sns.acceptance.post.PostSteps.게시물_등록_요청;
import static com.project.sns.acceptance.post.PostSteps.게시물_수정_요청;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

class PostAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @BeforeEach
    void before() {
        회원가입_요청(20, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");
    }


    @DisplayName("로그인이 성공한 회원은 게시물 등록을 시도하면 성공한다.")
    @Test
    void postSaveTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        List<ImageRequest> IMAGE_REQUESTS = new ArrayList<>();
        IMAGE_REQUESTS.add(new ImageRequest("//"));

        // when
        var response = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(IMAGE_REQUESTS, "내용이다!"));

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @DisplayName("로그인이 성공하지 못한 회원은 게시물 등록을 시도하면 실패한다.")
    @Test
    void postSaveExceptionTest() {

        // when
        var 유효하지_않은_토큰 = "kkkkkkkk";
        List<ImageRequest> IMAGE_REQUESTS = new ArrayList<>();
        var response = 게시물_등록_요청(유효하지_않은_토큰, new PostCreateRequest(IMAGE_REQUESTS, "내용이다"));

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인이 성공한 회원은 게시물 수정을 시도하면 성공한다.")
    @Test
    void postEditTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        List<ImageRequest> IMAGE_REQUESTS = new ArrayList<>();
        IMAGE_REQUESTS.add(new ImageRequest("//"));
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(IMAGE_REQUESTS, "내용이다.")).jsonPath().getLong("id");

        // when
        var response = 게시물_수정_요청(유효한_토큰, "바뀐 내용이다.", 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

}
