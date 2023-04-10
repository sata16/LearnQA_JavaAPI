package old_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Task12Header {

    @Test
    public void testHeader(){
        Response response = RestAssured
                .get(" https://playground.learnqa.ru/api/homework_header")
                .andReturn();
//        Headers responseHeader = response.getHeaders();
//        System.out.println(responseHeader);
        String someHeader = response.getHeader("x-secret-homework-header");
        assertEquals("Some secret value", someHeader, "invalid Header = " + someHeader);


    }

}
