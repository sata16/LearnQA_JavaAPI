package tests;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
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

@Epic("User cases")
@Feature("Delete")

public class UserDeleteTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    @Description("Попытку удалить пользователя по ID, после авторизации под другим. " +
            "Его данные для авторизации: (vinkotov@example.com / 1234)")
    @DisplayName("Удалить пользователя c ID=2")
    public void testDeleteUserId2(){

        //login
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        String jsonString = responseGetAuth.getBody().asString();
        int userId = JsonPath.from(jsonString).get("user_id");
        //System.out.println(userId);

        //delete
        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequestIdInt("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"),userId);

        responseDeleteUser.print();
        System.out.println(responseDeleteUser.statusCode());
        Assertions.assertResponseCodeEquals(responseDeleteUser,400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @Test
    @Description("Создать пользователя, авторизоваться из-под него, удалить, " +
            "затем попробовать получить его данные по ID и убедиться, что пользователь " +
            "действительно удален.")
    @DisplayName("Создать-Авторизоваться-Удалить-Проверить")
    public  void testDeleteAuthUser(){
        //  data user
        Map<String, String> userData = DataGenerator.getRegistrationData();
        // create user
        JsonPath responseCreateUser = apiCoreRequests
                .makePostJsonRequest("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");

        //auth user
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuthUser = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //delete

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequestIdString("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuthUser, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser, "auth_sid"),userId);
        //get deleted user
        Response responseGetDeletedUser = apiCoreRequests
                .responseGetUserData("https://playground.learnqa.ru/api/user/",
                        this.getHeader(responseGetAuthUser, "x-csrf-token"),
                        this.getCookie(responseGetAuthUser, "auth_sid"),userId);
        //System.out.println(responseGetDeletedUser.statusCode());
        String jsonString = responseGetDeletedUser.getBody().asString();
        //System.out.println(jsonString);
        Assertions.assertResponseCodeEquals(responseGetDeletedUser,404);
        Assertions.assertResponseTextEquals(responseGetDeletedUser, jsonString);

        }

        @Test
        @Description("Негативный кейс, попробовать удалить пользователя, " +
                "будучи авторизованными другим пользователем.")
        @DisplayName("Удалить пользователя")
    public void checkDeleteWithAnotherUser(){
            //  data for user1
            Map<String, String> userData1 = DataGenerator.getRegistrationData();

            //data for user2
            String email = "vvv2@example.com";
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

            //delete data of user2 by user1
            Response responseDeleteUser = apiCoreRequests
                    .makeDeleteRequestIdString("https://playground.learnqa.ru/api/user/",
                            this.getHeader(responseGetAuthUser1, "x-csrf-token"),
                            this.getCookie(responseGetAuthUser1, "auth_sid"),userId2);
            System.out.println(responseDeleteUser.statusCode());
            System.out.println(responseDeleteUser.asString());
            responseDeleteUser.prettyPrint();

            //get data user2
            Response responseGetDeletedUser = apiCoreRequests
                    .responseGetUserData("https://playground.learnqa.ru/api/user/",
                            this.getHeader(responseGetAuthUser2, "x-csrf-token"),
                            this.getCookie(responseGetAuthUser2, "auth_sid"),userId2);


            System.out.println(responseGetDeletedUser.asString());
            System.out.println(responseGetDeletedUser.statusCode());


        }
}
