package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a POST-request")
    public Response makePostRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }
    @Step("Make a POST-request-Json")
    public JsonPath makePostJsonRequest(String url, Map<String, String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .jsonPath();
    }
    @Step("Make PUT-request")
    public Response responsePutEdit(Map<String, String> userData, String header, String cookie, String id) {
        return given()
                .filter(new AllureRestAssured())
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .body(userData)
                .put("https://playground.learnqa.ru/api/user/" + id)
                .andReturn();
    }
    @Step("Make get-request")
    public Response responseGetUserData(String url, String header, String cookie, String id) {
        return
                given()
                        .filter(new AllureRestAssured())
                        .header("x-csrf-token", header)
                        .cookie("auth_sid", cookie)
                        .get(url + id)
                        .andReturn();
    }
    @Step("Make delete-request Int ID")
    public Response makeDeleteRequestIdInt(String url, String header, String cookie, int id) {
        return
                given()
                        .filter(new AllureRestAssured())
                        .header("x-csrf-token", header)
                        .cookie("auth_sid", cookie)
                        .delete(url + id)
                        .andReturn();
    }
    @Step("Make delete-request String ID")
    public Response makeDeleteRequestIdString(String url, String header, String cookie, String id) {
        return
                given()
                        .filter(new AllureRestAssured())
                        .header("x-csrf-token", header)
                        .cookie("auth_sid", cookie)
                        .delete(url + id)
                        .andReturn();
    }

}
