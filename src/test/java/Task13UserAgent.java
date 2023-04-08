import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task13UserAgent {
    @ParameterizedTest
    @CsvSource({"'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', 'Web','Chrome','No'",
    "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30','Mobile', 'No', 'Android'",
    "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1','Mobile', 'Chrome', 'iOS'",
    "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', 'Googlebot', 'Unknown', 'Unknown'",
    "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1','Mobile','No', 'iPhone'"})

    public void testUserAgent1(String agent, String expectPlatform, String expectBrowser,String expectDevice){
        JsonPath responseGet = RestAssured
                .given()
                .header("User-Agent",agent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();
        responseGet.prettyPrint();
        String getPlatform = responseGet.get("platform");
        String getBrowser = responseGet.get("browser");
        String getDevice = responseGet.get("device");

        assertEquals(expectPlatform, getPlatform, "Platform is not equal to expected value = "+expectPlatform+" \nGet Value = "+getPlatform);
        assertEquals(expectBrowser, getBrowser, "Browser is not equal to expected value = "+expectBrowser+" \nGet Value = "+getBrowser);
        assertEquals(expectDevice, getDevice, "Device is not equal to expected value = "+expectDevice+" \nGet Value = "+getDevice);


    }
//Отладка
//    @Test
//    public void testUserAgent(){
//        Response response = RestAssured
//                .given()
//                .header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0")
//                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
//                .andReturn();
//        response.print();
//        response.prettyPrint();
//
//    }



}
