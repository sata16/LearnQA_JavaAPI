import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class FirstGet {
    @Test
    public void testFirstGet(){

        Response response = RestAssured.get("https://playground.learnqa.ru/api/get_text").andReturn();
        response.print();
        response.prettyPrint();


    }
}
