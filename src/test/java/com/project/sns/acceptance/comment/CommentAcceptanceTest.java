package com.project.sns.acceptance.comment;

import com.project.sns.acceptance.AcceptanceTest;
import com.project.sns.comment.repository.CommentRepository;
import com.project.sns.post.controller.dto.request.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static com.project.sns.acceptance.comment.CommentSteps.*;
import static com.project.sns.acceptance.post.PostSteps.게시물_등록_요청;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class CommentAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void before() {
        회원가입_요청(20, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");
    }

    @DisplayName("로그인에 성공한 회원은 댓글 작성 요청에 성공한다.")
    @Test
    void commentSaveTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        var response = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @DisplayName("로그인에 성공한 회원은 댓글 작성 요청 시 게시물이 존재하지 않을 경우 실패한다.")
    @Test
    void commentSaveExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);

        // when
        var response = 댓글_생성_요청(유효한_토큰, "첫번째 댓글..", 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @DisplayName("로그인에 실패한 회원은 댓글 작성 요청에 실패한다.")
    @Test
    void commentSaveExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 댓글_생성_요청(유효하지_않은_토큰, "첫번째 댓글..", 첫번째_게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 성공한 회원은 댓글 수정 요청에 성공한다.")
    @Test
    void editCommentTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        var 첫번째_댓글_id = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        // when
        var response = 댓글_수정_요청(다른_유효한_토큰, "수정된 첫번째 댓글", 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getString("comment")).isEqualTo("수정된 첫번째 댓글")
        );


    }

    @DisplayName("로그인에 성공한 회원이 댓글 수정 요청 시 댓글이 존재하지 않는다면 댓글 수정 요청에 실패한다.")
    @Test
    void editCommentExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");


        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        var response = 댓글_수정_요청(다른_유효한_토큰, "수정된 첫번째 댓글", 첫번째_게시물_id, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @DisplayName("로그인에 성공한 회원이 댓글 수정 요청 시 본인이 작성한 댓글이 아니라면 댓글 수정 요청에 실패한다.")
    @Test
    void editCommentExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");
        var 첫번째_댓글_id = 댓글_생성_요청(유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        var response = 댓글_수정_요청(다른_유효한_토큰, "수정된 첫번째 댓글", 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 실패한 회원은 댓글 수정 요청 시 댓글 수정 요청에 실패한다.")
    @Test
    void editCommentExceptionTest3() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        var 첫번째_댓글_id = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 댓글_수정_요청(유효하지_않은_토큰, "수정된 첫번째 댓글", 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 성공한 회원은 댓글 삭제 요청 시 댓글 삭제 요청에 성공한다.")
    @Test
    void deleteCommentTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        var 첫번째_댓글_id = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        // when
        var response = 댓글_삭제_요청(다른_유효한_토큰, 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value()),
                () -> assertThat(commentRepository.findAll()).isEmpty()
        );
    }

    @DisplayName("로그인에 실패한 경우 자신이 작성한 댓글 삭제 시 실패한다.")
    @Test
    void deleteCommentExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        var 첫번째_댓글_id = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 댓글_삭제_요청(유효하지_않은_토큰, 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("자신이 작성한 댓글이 아닐 경우 댓글 삭제 시 실패한다.")
    @Test
    void deleteCommentExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        var 첫번째_댓글_id = 댓글_생성_요청(다른_유효한_토큰, "첫번째 댓글..", 첫번째_게시물_id).jsonPath().getLong("id");

        // when
        var response = 댓글_삭제_요청(유효한_토큰, 첫번째_게시물_id, 첫번째_댓글_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
