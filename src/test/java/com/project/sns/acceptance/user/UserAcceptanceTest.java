package com.project.sns.acceptance.user;

import com.project.sns.acceptance.AcceptanceTest;
import com.project.sns.post.controller.dto.request.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static com.project.sns.acceptance.comment.CommentSteps.댓글_생성_요청;
import static com.project.sns.acceptance.post.PostSteps.게시물_등록_요청;
import static com.project.sns.acceptance.post.PostSteps.게시물_좋아요_누르기_요청;
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

    @Test
    @DisplayName("로그인에 성공한 회원은 댓글 알림 조회 요청에 성공한다.")
    void getCommentAlarmListTest() {

        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id);

        var response = 알림_조회_기능_요청(유효한_토큰);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getInt("content.get(0).id")).isEqualTo(1),
                () -> assertThat(response.jsonPath().getString("content.get(0).alarmType")).isEqualTo("NEW_COMMENT_ON_POST"),
                () -> assertThat(response.jsonPath().getInt("content.get(0).args.fromUserId")).isEqualTo(2),
                () -> assertThat(response.jsonPath().getInt("content.get(0).args.targetId")).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("로그인에 실패한 회원은 댓글 알림 조회 요청에 실패한다.")
    void getCommentAlarmListExceptionTest() {

        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id);

        var 유효하지_않은_토큰 = "kkk..";
        var response = 알림_조회_기능_요청(유효하지_않은_토큰);

        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("로그인에 성공한 회원은 좋아요 알림 조회 요청에 성공한다.")
    void getLikeAlarmListTest() {

        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);

        var response = 알림_조회_기능_요청(유효한_토큰);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getInt("content.get(0).id")).isEqualTo(1),
                () -> assertThat(response.jsonPath().getString("content.get(0).alarmType")).isEqualTo("NEW_LIKE_ON_POST"),
                () -> assertThat(response.jsonPath().getInt("content.get(0).args.fromUserId")).isEqualTo(2),
                () -> assertThat(response.jsonPath().getInt("content.get(0).args.targetId")).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("로그인에 실패한 회원은 좋아요 알림 조회 요청에 실패한다.")
    void getLikeAlarmListExceptionTest() {

        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);

        var 유효하지_않은_토큰 = "kkk..";
        var response = 알림_조회_기능_요청(유효하지_않은_토큰);

        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
