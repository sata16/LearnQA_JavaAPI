package old_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task11Cookie {
    @Test
    public void testCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

//        Map<String,String> responseCookie = response.getCookies();
//        System.out.println(responseCookie);
        String responseCookies = response.getCookie("HomeWork");
        assertEquals("hw_value",responseCookies, "invalid cookies");

    }




}
