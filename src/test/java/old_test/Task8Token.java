package old_test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;




public class Task8Token {

    @Test
    public void testToken()  {

        Response response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        String jsonString = response.getBody().asString();
        String token = JsonPath.from(jsonString).get("token");
       int second = JsonPath.from(jsonString).get("seconds");
        response.prettyPrint();
        System.out.println(second);

        System.out.println(second * 1000);

        for (int i = 1; i < 3; i++){
            JsonPath response1 = RestAssured
                    .given()
                    .queryParam("token",token)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();
            response1.prettyPrint();
            String status =  response1.get("status");
            if (status.equals("Job is NOT ready")){
                System.out.println(status);
                try {
                    Thread.sleep(second * 1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }else{
               Boolean statusTrue = response1.get("status").equals("Job is ready");
               String result = response1.get("result");
               //System.out.println(response1.get("status").equals("Job is ready"));
               System.out.println("Status = " + statusTrue + "\nresult = " + result);
            }

       }

    }
}
