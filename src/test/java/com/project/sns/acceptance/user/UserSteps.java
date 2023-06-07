package com.project.sns.acceptance.user;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class UserSteps {

    public static ExtractableResponse<Response> 베어러_인증_로그인_요청(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/users/login")
                .then().log().all().extract();
    }

    public static String 베어러_인증_응답에서_token_가져오기(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("token");
    }

    public static ExtractableResponse<Response> 회원가입_요청(int age, String email, String password, String userName, String nickName, String address, String profileMessage, String profileImage) {
        Map<String, String> params = new HashMap<>();
        params.put("age", age + "");
        params.put("email", email);
        params.put("password", password);
        params.put("userName", userName);
        params.put("nickName", nickName);
        params.put("address", address);
        params.put("profileMessage", profileMessage);
        params.put("profileImage", profileImage);

        return RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/users/signup")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰_인증으로_내_회원정보_조회_요청(String token) {
        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                        token)
                .accept(APPLICATION_JSON_VALUE)
                .when().get("/api/users/my")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 토큰_인증으로_내_회원정보_수정_요청(String token, String profileMessage, String profileImage, String nickName) {
        Map<String, String> params = new HashMap<>();
        params.put("profileMessage", profileMessage + "");
        params.put("profileImage", profileImage);
        params.put("nickName", nickName);

        return RestAssured.given().log().all()
                .headers("Authorization",
                        "Bearer " +
                                token)
                .contentType(APPLICATION_JSON_VALUE)
                .body(params)
                .when().patch("/api/users/my")
                .then().log().all().extract();
    }
}
