package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectidValue){
        Response.then().assertThat().body("$",hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectidValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }
    //проверим, что при создании пользователя приходит поле с именем id
    public static void assertJsonHasField(Response Response, String expectidFieldName){
        Response.then().assertThat().body("$", hasKey(expectidFieldName));
    }
    //перебираем сразу все значения
    public static  void assertJsonHasFields(Response Response, String[] expectedFieldNames){
        for (String expectedFieldName : expectedFieldNames){
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }
    //неавториз пользоват неполучает какое-то поле
    public static void assertJsonHasNotField(Response Response, String unexpectedFieldName){
        Response.then().assertThat().body("$", not(hasKey(unexpectedFieldName)));
    }
//некорректный формат

    public static void assertResponseFormatEmail(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }

}
