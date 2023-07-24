package com.project.sns.acceptance.comment;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CommentSteps {

    public static ExtractableResponse<Response> 댓글_생성_요청(String token, String comment, Long postId) {
        Map<String, String> params = new HashMap<>();
        params.put("comment", comment);

        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/posts/{postId}/comments", postId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 댓글_수정_요청(String token, String comment, Long postId, Long commentId) {
        Map<String, String> params = new HashMap<>();
        params.put("comment", comment);

        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().patch("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 댓글_삭제_요청(String token, Long postId, Long commentId) {
        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .when().delete("/api/posts/{postId}/comments/{commentId}", postId, commentId)
                .then().log().all().extract();
    }
}
