package FirstDemo.files;

import FirstDemo.files.Files.ReusableMethods;
import Pojo.Api;
import Pojo.GetCourse;
import Pojo.WebAutomation;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;

public class OauthTest {
    public static void main(String[] args) throws InterruptedException {
        //https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&access_type=offline

        //Get authorization code
        String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AY0e-g5dR2H82DsFvHwM1qk8GM9BUxfUybFAEx_EiZyB72pSV_39V96aeIszS2sxksXAng&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none";
        //String url = urlGoogle;
        String partialCode = url.split("code=")[1];
        System.out.println(partialCode);
        String code = partialCode.split("&scope")[0];
        System.out.println(code);

        //Get access token
        String accessTokenResponse = given().urlEncodingEnabled(false).header("Content-Type","application/x-www-form-urlencoded")
                .queryParams("code",code)
                .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
        .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = ReusableMethods.rawToJson(accessTokenResponse);
        String accessToken = js.get("access_token");
        System.out.println(accessTokenResponse);

        //Get courses
        GetCourse response = given().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        System.out.println(response.getLinkedIn());
        System.out.println(response.getCourses());
        System.out.println(response.getCourses().getWebAutomation().size());
        System.out.println(response.getCourses().getWebAutomation().get(1).getCourseTitle());

        List<Api> apiCourses=response.getCourses().getApi();
        for(Api course:apiCourses){
            if(course.getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")){
                System.out.println("The price of the course is: $"+course.getPrice());
                break;
            }
        }

        //Compare courses list
        String[] courses = {"Selenium Webdriver Java","Cypress","Protractor"};

        ArrayList<String> actualCourses = new ArrayList<>();
        List<WebAutomation> automationsCourses = response.getCourses().getWebAutomation();
        for(WebAutomation course:automationsCourses){
            actualCourses.add(course.getCourseTitle());
        }

        List<String> expectedCourses = Arrays.asList(courses);
        Assert.assertTrue(actualCourses.equals(expectedCourses));

    }
}
