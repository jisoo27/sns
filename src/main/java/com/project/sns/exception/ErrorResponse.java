package com.project.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private final int code;
    private final String message;

    public static ErrorResponse of (int code, String message) {
        return new ErrorResponse(code, message);
    }

}
