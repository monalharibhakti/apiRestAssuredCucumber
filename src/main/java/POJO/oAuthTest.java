package POJO;

import POJO.GetCourse;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.List;

import static io.restassured.RestAssured.*;

public class oAuthTest {

    public static void main(String[] args) throws InterruptedException {

        String[] courseTitles= { "Selenium Webdriver Java","Cypress","Protractor"};
        System.setProperty("webdriver.chrome.driver", "D://chromedriver.exe");
        WebDriver driver= new ChromeDriver();
        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys("srinath19830");
        driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys("");
        driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
        Thread.sleep(4000);
        String url=driver.getCurrentUrl();
        String partialcode=url.split("code=")[1];
        String code=partialcode.split("&scope")[0];
        System.out.println(code);



        String accessTokenResponse = given().urlEncodingEnabled(false)
                .queryParams("code", code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();
        JsonPath js = new JsonPath(accessTokenResponse);
        String accessToken = js.getString("access_token");

        // provide defaultParses as json if you want to expect json in response
        // defaultParse -> if content-type is json, then you can avoid
        GetCourse gcResponse = given().log().all().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        gcResponse.getLinkedin();
        List<Api> allApi = gcResponse.getCourses().getApi();

        for(int i=0;i<allApi.size();i++){
            if(allApi.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
            {
                System.out.println(allApi.get(i).getPrice());
                break;
            }

        }

        List<WebAutomation> webAutomation = gcResponse.getCourses().getWebAutomation();
        for(int i=0;i<webAutomation.size();i++){
            System.out.println(webAutomation.get(i).getCourseTitle());
        }


    }

}
