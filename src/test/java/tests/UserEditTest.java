package tests;
import io.restassured.path.json.JsonPath;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //Generate user
        Map<String, String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        String userId = responseCreateAuth.getString("id");

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //edit
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        System.out.println(responseUserData.asString());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @DisplayName("Попытаемся изменить данные пользователя, будучи неавторизованными")
    public void checkEditWithoutAuth() {
//создала пользователя и получила id
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateAuth.getString("id");

        // Изменяю значение у созданного пользователя
        Map<String, String> userDataPut = new HashMap<>();
        userData.put("firstName", "Changed name");

        Response editResponse = apiCoreRequests.responsePutEdit(userDataPut, "", "", userId);

        System.out.println(editResponse.statusCode());
        Assertions.assertResponseCodeEquals(editResponse, 400);

    }

    @Test
    @DisplayName("Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем")
    public void checkEditWithAnotherUser() {
        //  data for user1
        Map<String, String> userData1 = DataGenerator.getRegistrationData();

        //data for user2
        String email = "aaaaaa7@example.com";
        Map<String, String> userData2 = new HashMap<>();
        userData2.put("email", email);
        userData2 = DataGenerator.getRegistrationData(userData2);
        System.out.println(userData1);
        System.out.println(userData2);
        //create users
        JsonPath responseCreateUser1 = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData1);

        JsonPath responseCreateUser2 = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData2);
        String userId1 = responseCreateUser1.getString("id");
        String userId2 = responseCreateUser2.getString("id");
        // System.out.println(userId1);
        // System.out.println(userId2);

        //auth user1
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", userData1.get("email"));
        authData1.put("password", userData1.get("password"));

        Response responseGetAuthUser1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);
        // auth user2
        Map<String, String> authData2 = new HashMap<>();
        authData2.put("email", userData2.get("email"));
        authData2.put("password", userData2.get("password"));

        Response responseGetAuthUser2 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData2);

        //edit data of user2 by user1
        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .responsePutEdit(editData, this.getHeader(responseGetAuthUser1, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser1, "auth_sid"), userId2);
        // System.out.println(responseEditUser.statusCode());

        //get data user2
        Response responseUserData = apiCoreRequests
                .responseGetUserData("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuthUser2, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser2, "auth_sid"), userId2);


        System.out.println(responseUserData.asString());
        System.out.println(responseUserData.statusCode());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }

    @Test
    @DisplayName("Попытаемся изменить email пользователя, будучи авторизованными тем же " +
            "пользователем, на новый email без символа @ ")
    public void checkEditInvalidEmail() {
        //  data user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        // create user
        JsonPath responseCreateUser = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");

        //auth user1
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuthUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

    //edit email (email без @)
    String newEmail = "rrrrexample.com";
    Map<String, String> editData = new HashMap<>();
        editData.put("email", newEmail);

    Response responseEditUser = apiCoreRequests
            .responsePutEdit(editData, this.getHeader(responseGetAuthUser, "x-csrf-token"),
                    this.getCookie(responseGetAuthUser, "auth_sid"), userId);
        //get newEmail
        Response responseUserData = apiCoreRequests
                .responseGetUserData("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuthUser, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser, "auth_sid"), userId);


        System.out.println(responseUserData.asString());
        System.out.println(responseUserData.statusCode());
        Assertions.assertJsonByName(responseUserData, "email", newEmail);

}

    @Test
    @DisplayName("Попытаемся изменить firstName пользователя, будучи авторизованными " +
            "тем же пользователем, на очень короткое значение в один символ")
    public void checkEditInvalidFirstName() {
        //  data user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        // create user
        JsonPath responseCreateUser = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");

        //auth user1
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuthUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //edit email (email без @)
        String newName = "f";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .responsePutEdit(editData, this.getHeader(responseGetAuthUser, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser, "auth_sid"), userId);
        //get newEmail
        Response responseUserData = apiCoreRequests
                .responseGetUserData("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuthUser, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser, "auth_sid"), userId);


        System.out.println(responseUserData.asString());
        System.out.println(responseUserData.statusCode());
        Assertions.assertJsonByName(responseUserData, "firstName", newName);

    }
}
