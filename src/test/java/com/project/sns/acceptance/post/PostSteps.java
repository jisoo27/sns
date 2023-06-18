package com.project.sns.acceptance.post;

import com.project.sns.post.controller.dto.request.PostCreateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class PostSteps {

    public static ExtractableResponse<Response> 게시물_등록_요청(String token, PostCreateRequest postCreateRequest) {
        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .body(postCreateRequest)
                .when().post("/api/posts")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 게시물_수정_요청(String token, String content, Long postId) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);

        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().patch("/api/posts/{postId}", postId)
                .then().log().all().extract();
    }
}
