package FirstDemo.files;

import FirstDemo.files.Files.ReusableMethods;
import Pojo.Api;
import Pojo.GetCourse;
import Pojo.WebAutomation;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.*;

public class OauthTest {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        PrintStream log = new PrintStream(new FileOutputStream("log.txt"));
        RequestSpecification req = new RequestSpecBuilder().addFilter(ResponseLoggingFilter.logResponseTo(log)).build();


        //https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&access_type=offline

        //Get authorization code
        String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AY0e-g7hpQrt3FbaooZNfK5CucwNqaJv94V6x1qjdA3QCxiCZX_aAHB6rEoCiSY9Ldtuaw&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=1&prompt=none";
        //String url = urlGoogle;
        String partialCode = url.split("code=")[1];
        System.out.println(partialCode);
        String code = partialCode.split("&scope")[0];
        System.out.println(code);

        //Get access token
        String accessTokenResponse = given().log().all().urlEncodingEnabled(false).header("Content-Type","application/x-www-form-urlencoded")
                .queryParams("code",code)
                .queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
        .when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();

        JsonPath js = ReusableMethods.rawToJson(accessTokenResponse);
        String accessToken = js.get("access_token");
        System.out.println(accessTokenResponse);

        //Get courses deserialize
        GetCourse response = given().spec(req).queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        System.out.println(response.getLinkedIn());
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
        Assert.assertEquals(expectedCourses, actualCourses);

    }
}
