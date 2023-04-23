package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;

import java.util.HashMap;
import java.util.Map;

@Epic("Authorisation cases")
@Feature("Authorization")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123" );
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        //System.out.println(responseCreateAuth.asString());
        //System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" +email+"' already exists");

    }
    //создаем пользователя с помощью DataGeneration getRegistrationData
    @Test
    public void testCreateUserWithExistingEmail1(){
        String email = "vinkotov@example.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGenerator.getRegistrationData(userData);

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" +email+"' already exists");

    }

    @Test
    public void testCreateUserSuccessfully(){
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123" );
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName", "learnqa");

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth,"id");

    }
//создаем пользователя с помощью DataGeneration getRegistrationData
    @Test
    public void testCreateUserSuccessfully1(){
        String email = DataGenerator.getRandomEmail();
        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        //System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasKey(responseCreateAuth,"id");

    }

    
    @Test
    public void testCreateUserIncorrectFormatEmail(){
        String email = "vinkotovexample.com";
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", "123" );
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName", "learnqa");


        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        //System.out.println(responseCreateAuth.asString());
        //System.out.println(responseCreateAuth.statusCode());

        Assertions.assertResponseFormatEmail(responseCreateAuth, "Invalid email format");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);


    }

    @ParameterizedTest
    @CsvSource({",'123','lernqa', 'lernqa',  'lernqa'",
            " 'lernqa@gmail.com', , 'lernqa',  'lernqa', 'lernqa'",
            " 'lernqa@gmail.com', '123', ,'lernqa', 'lernqa'",
            " 'lernqa@gmail.com', '123', 'lernqa', ,  'lernqa'",
            " 'lernqa@gmail.com', '123','lernqa',  'lernqa', "
    })
    @DisplayName("Создание пользователя без указания одного из полей")
    public void checkAllFieldsNotNull(String email, String password,
                                      String username, String firstName, String lastName){
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("password", password);
        userData.put("username",username);
        userData.put("firstName",firstName);
        userData.put("lastName", lastName);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);

        //responseCreateAuth.print();
        //System.out.println(responseCreateAuth.statusCode());

        if(email == null){
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: email");
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        } else if (password == null) {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: password");
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        }else if (username == null) {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: username");
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        }else if (firstName == null) {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: firstName");
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        }
        else if (lastName == null) {
            Assertions.assertResponseTextEquals(responseCreateAuth, "The following required params are missed: lastName");
            Assertions.assertResponseCodeEquals(responseCreateAuth, 400);
        }else {
            System.out.println("Все параметры есть");
        }

    }

   @Test
    @DisplayName("Создание пользователя с очень коротким именем в один символ")
    public void checkShortUsername(){
        String username = "t";
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
        //responseCreateAuth.print();
        //System.out.println(responseCreateAuth.statusCode());

       Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
       Assertions.assertResponseCodeEquals(responseCreateAuth, 400);

    }

    @Test
    @DisplayName("Создание пользователя с именем > 250 симв")
    public void checkLongUsername(){
        String username = "learnqa";
        //создаю имя 252 символа
        for (int i = 1; username.length()<252; i++){
            username+= "learnqa";
        }
        //System.out.println(username);
        //System.out.println(username.length());
        Map<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData = DataGenerator.getRegistrationData(userData);
        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/",userData);
        //responseCreateAuth.print();
        //System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
        Assertions.assertResponseCodeEquals(responseCreateAuth, 400);

    }

}
