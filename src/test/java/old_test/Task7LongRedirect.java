package old_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Task7LongRedirect {

    @Test
    public void testLongRedirect(){
        String urlTest = "https://playground.learnqa.ru/api/long_redirect";
       do {
           Response response = RestAssured
                   .given()
                   .redirects()
                   .follow(false)
                   .when()
                   .get(urlTest)
                   .andReturn();
           int statusCode = response.getStatusCode();
           System.out.println(statusCode);
           urlTest = response.getHeader("Location");
           System.out.println(urlTest);
       } while (urlTest != null);


    }


}
