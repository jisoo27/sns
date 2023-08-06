package com.project.sns.acceptance.post;

import com.project.sns.acceptance.AcceptanceTest;
import com.project.sns.post.controller.dto.request.PostCreateRequest;
import com.project.sns.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import static com.project.sns.acceptance.post.PostSteps.*;
import static com.project.sns.acceptance.user.UserSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.*;

class PostAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void before() {
        회원가입_요청(22, EMAIL, PASSWORD, "admin", "admmin", "경기도 수원시", "안녕", "/");
    }


    @DisplayName("로그인이 성공한 회원은 게시물 등록을 시도하면 성공한다.")
    @Test
    void postSaveTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);

        // when
        var response = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("dd", "ㅇㅇ"),"내용이다!"));

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @DisplayName("로그인이 성공하지 못한 회원은 게시물 등록을 시도하면 실패한다.")
    @Test
    void postSaveExceptionTest() {

        // when
        var 유효하지_않은_토큰 = "kkkkkkkk";
        var response = 게시물_등록_요청(유효하지_않은_토큰, new PostCreateRequest(List.of("dd", "ㅇㅇ"), "내용이다"));

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인이 성공한 회원은 게시물 수정을 시도하면 성공한다.")
    @Test
    void postEditTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("ss", "//"), "내용이다.")).jsonPath().getLong("id");

        // when
        var response = 게시물_수정_요청(유효한_토큰, "바뀐 내용이다.", 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @DisplayName("로그인이 성공한 회원은 자신이 쓴 글을 조회를 시도하면 성공한다.")
    @Test
    void getMyPostListTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "두번째 내용"));

        // when
        var response = 나의_게시물_조회_요청(유효한_토큰);

        // then
        assertAll(
                () -> assertThat(response.jsonPath().getLong("content.get(0).id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("content.get(0).content")).isEqualTo("내용이다."),
                () -> assertThat(response.jsonPath().getLong("content.get(1).id")).isEqualTo(2L),
                () -> assertThat(response.jsonPath().getString("content.get(1).content")).isEqualTo("두번째 내용")
        );
    }

    @DisplayName("로그인이 실패한 회원은 자신이 쓴 글을 조회를 시도하면 실패한다.")
    @Test
    void getMyPostListExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("dd", "//"), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("ss", "ff"), "두번째 내용"));

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 나의_게시물_조회_요청(유효하지_않은_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인이 성공한 회원은 자신이 작성한 게시물 삭제 시 성공한다.")
    @Test
    void postDeleteTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("ss", "//"), "내용이다.")).jsonPath().getLong("id");

        // when
        var response = 나의_게시물_삭제_요청(유효한_토큰, 게시물_id);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value()),
                () -> assertThat(postRepository.findAll()).isEmpty()
        );
    }

    @DisplayName("로그인에 실패한 경우 자신이 작성한 게시물 삭제 시 실패한다.")
    @Test
    void postDeleteExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("ss", "//"), "내용이다.")).jsonPath().getLong("id");

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 나의_게시물_삭제_요청(유효하지_않은_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("자신이 작성한 게시물이 아닐 경우 게시물 삭제 시 실패한다.")
    @Test
    void postDeleteExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        String anotherEmail = "anotherAdmin@email.com";
        String anotherPassword = "kkk";
        회원가입_요청(27, anotherEmail, anotherPassword, "userName", "nickName", "address", "hi", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청(anotherEmail, anotherPassword);
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        var response = 나의_게시물_삭제_요청(다른_유효한_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인이 성공한 회원은 게시물에 좋아요 요청 시 성공한다.")
    @Test
    void postLikeTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        var response = 게시물_좋아요_누르기_요청(다른_유효한_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @DisplayName("로그인에 실패한 경우 좋아요 요청 시 실패한다.")
    @Test
    void postLikeExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 게시물_좋아요_누르기_요청(유효하지_않은_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("좋아요 요청 시 게시물이 없는 경우 좋아요 요청에 실패한다.")
    @Test
    void postLikeExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);

        // when
        var response = 게시물_좋아요_누르기_요청(유효한_토큰, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @DisplayName("이미 좋아요가 되어있을 때 다시 좋아요를 요청을 할 경우 요청에 실패한다.")
    @Test
    void postLikeExceptionTest3() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);

        // when
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 게시물_id);
        var response = 게시물_좋아요_누르기_요청(다른_유효한_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(CONFLICT.value());
    }

    @DisplayName("로그인이 성공한 회원은 좋아요를 눌렀던 게시물에 좋아요 취소를 요청할 경우 성공한다.")
    @Test
    void PostNotLikeTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 게시물_id);

        // when
        var response = 게시물_좋아요_취소_요청(다른_유효한_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    @DisplayName("로그인에 실패한 경우 좋아요 취소 요청에 실패한다.")
    @Test
    void postNotLikeExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 게시물_id);

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 게시물_좋아요_취소_요청(유효하지_않은_토큰, 게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("좋아요 취소 요청 시 게시물이 없는 경우 좋아요 취소 요청에 실패한다.")
    @Test
    void postNotLikeExceptionTest2() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);

        // when
        var response = 게시물_좋아요_취소_요청(유효한_토큰, 1L);

        // then
        assertThat(response.statusCode()).isEqualTo(NOT_FOUND.value());
    }

    @DisplayName("로그인을 성공한 회원은 자신이 좋아요 한 게시글 조회 시 요청에 성공한다.")
    @Test
    void getMyLikePostTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");
        var 두번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("dd", "kk"), "두번째 내용")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 두번째_게시물_id);

        // when
        var response = 내가_좋아요_한_게시물_조회_요청(다른_유효한_토큰);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getLong("content.get(0).id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("content.get(0).content")).isEqualTo("내용이다."),
                () -> assertThat(response.jsonPath().getLong("content.get(1).id")).isEqualTo(2L),
                () -> assertThat(response.jsonPath().getString("content.get(1).content")).isEqualTo("두번째 내용")
        );
    }

    @DisplayName("로그인에 실패한 회원은 자신이 좋아요 한 게시글 조회 시 요청에 실패한다.")
    @Test
    void getMyLikePostExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");
        var 두번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("dd", "kk"), "두번째 내용")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 두번째_게시물_id);

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 내가_좋아요_한_게시물_조회_요청(유효하지_않은_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 성공한 회원은 게시물의 좋아요 개수 조회 요청에 성공한다.")
    @Test
    void getPostLikeCount() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);

        // when
        var response = 게시물_좋아요_개수_조회_요청(다른_유효한_토큰, 첫번째_게시물_id);

        // then
        int 좋아요_개수 = response.jsonPath().getInt("count");
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(좋아요_개수).isEqualTo(1)
        );
    }

    @DisplayName("로그인에 실패한 회원은 좋아요 개수 조회 요청에 실패한다.")
    @Test
    void getPostLikeCountExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        var 첫번째_게시물_id = 게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of("//", "ss"), "내용이다.")).jsonPath().getLong("id");

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_좋아요_누르기_요청(다른_유효한_토큰, 첫번째_게시물_id);

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 게시물_좋아요_개수_조회_요청(유효하지_않은_토큰, 첫번째_게시물_id);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 성공한 회원은 모든 글 조회 요청에 성공한다.")
    @Test
    void getAllPostTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "두번째 내용"));

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "세번째 내용"));
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "네번째 내용"));

        // when
        var response = 모든_게시물_조회_요청(유효한_토큰);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getLong("content.get(0).id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("content.get(0).content")).isEqualTo("내용이다."),
                () -> assertThat(response.jsonPath().getLong("content.get(1).id")).isEqualTo(2L),
                () -> assertThat(response.jsonPath().getString("content.get(1).content")).isEqualTo("두번째 내용"),
                () -> assertThat(response.jsonPath().getLong("content.get(2).id")).isEqualTo(3L),
                () -> assertThat(response.jsonPath().getString("content.get(2).content")).isEqualTo("세번째 내용"),
                () -> assertThat(response.jsonPath().getLong("content.get(3).id")).isEqualTo(4L),
                () -> assertThat(response.jsonPath().getString("content.get(3).content")).isEqualTo("네번째 내용")
                );
    }

    @DisplayName("로그인에 실패한 회원은 모든 글 조회 요청에 실패한다.")
    @Test
    void getAllPostExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "두번째 내용"));

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "세번째 내용"));
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "네번째 내용"));

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 모든_게시물_조회_요청(유효하지_않은_토큰);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

    @DisplayName("로그인에 성공한 회원은 회원의 나이 대 별 글 조회 요청에 성공한다.")
    @Test
    void getUserAgeFilterTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "두번째 내용"));

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "세번째 내용"));
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "네번째 내용"));

        // when
        var response = 회원_나이_별_게시물_조회_요청(유효한_토큰, 20, 30);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                () -> assertThat(response.jsonPath().getLong("content.get(0).id")).isEqualTo(1L),
                () -> assertThat(response.jsonPath().getString("content.get(0).content")).isEqualTo("내용이다."),
                () -> assertThat(response.jsonPath().getLong("content.get(1).id")).isEqualTo(2L),
                () -> assertThat(response.jsonPath().getString("content.get(1).content")).isEqualTo("두번째 내용")
        );
    }

    @DisplayName("로그인에 실패한 회원은 회원의 나이 대 별 글 조회 요청에 실패한다.")
    @Test
    void getUserAgeFilterExceptionTest() {

        // given
        var 로그인_요청 = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        var 유효한_토큰 = 베어러_인증_응답에서_token_가져오기(로그인_요청);
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "내용이다."));
        게시물_등록_요청(유효한_토큰, new PostCreateRequest(List.of(), "두번째 내용"));

        회원가입_요청(30, "anotherAdmin@email.com", "kkk", "sosomi", "somin", "경기도 용인시", "반가워", "/");
        var 다른_로그인_요청 = 베어러_인증_로그인_요청("anotherAdmin@email.com", "kkk");
        var 다른_유효한_토큰 = 베어러_인증_응답에서_token_가져오기(다른_로그인_요청);
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "세번째 내용"));
        게시물_등록_요청(다른_유효한_토큰, new PostCreateRequest(List.of(), "네번째 내용"));

        // when
        var 유효하지_않은_토큰 = "kkk..";
        var response = 회원_나이_별_게시물_조회_요청(유효하지_않은_토큰, 20, 30);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }

}
