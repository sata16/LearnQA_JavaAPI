import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Task5Json {
    @Test
    public void testTask5(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();

        response.print();
        response.prettyPrint();

    }

    @Test
    public void testTask5Json(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        Map<String, String> params = new HashMap<>();
       // response.prettyPrint();
        params = response.get("messages[1]");
//        System.out.println(params);
//        String answer = params.get("message");
//        System.out.println(answer);
        System.out.println(params.get("message"));
    }
}
