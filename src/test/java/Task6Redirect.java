import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Task6Redirect {

    @Test
    public void testRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
      // response.print();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        String location = response.getHeader("Location");
        System.out.println(location);
    }

}
