package tests;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import io.restassured.http.Headers;
import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testCetUserDetailsAuthUser() {
        String email = "vinkotov@example.com";
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put("password", "1234");
        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
        responseGetAuth.print();
//        Headers responseHeader = responseGetAuth.getHeaders();
//        System.out.println(responseHeader);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", header)
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();

        responseUserData.print();
        String[] expectedFields = {"username", "email", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
//        Assertions.assertJsonHasField(responseUserData,"username");
//        Assertions.assertJsonHasField(responseUserData,"firstName");
//        Assertions.assertJsonHasField(responseUserData,"lastName");
//        Assertions.assertJsonHasField(responseUserData,"email");


    }


    @Test
    @DisplayName("авторизация и получение данных по разным id")
    public void checkAuth() {
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData);
     //responseCreateAuth.prettyPrint();
        String userId = responseCreateAuth.getString("id");


        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/"+userId)
                .andReturn();
       responseUserData.print();

    }
}
