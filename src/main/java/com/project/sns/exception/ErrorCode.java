package com.project.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_EMAIL(CONFLICT, "이미 가입되어있는 이메일입니다."),
    USER_NOT_FOUND(NOT_FOUND, "가입되지 않은 유저입니다."),
    POST_NOT_FOUND(NOT_FOUND, "찾는 게시글이 존재하지 않습니다"),
    INVALID_PASSWORD(UNAUTHORIZED, "비밀번호가 유효하지 않습니다"),
    INVALID_PERMISSION(UNAUTHORIZED, "권한이 유효하지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다."),
    IMAGE_NOT_FOUND(NOT_FOUND, "삭제할 이미지가 존재하지 않습니다."),
    ALREADY_LIKED(CONFLICT, "사용자가 이미 게시물에 좋아요를 누른 상태입니다."),
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다");

    private HttpStatus status;
    private String message;
}
