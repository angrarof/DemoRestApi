package FirstDemo.files;

import FirstDemo.files.Files.Payload;
import FirstDemo.files.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) throws IOException {
        RestAssured.baseURI="https://rahulshettyacademy.com";

        //Add place
        String response = given().log().all().queryParam("key","qaclick123")
                    .header("Content-Type","application/json")
                    .body(new String(Files.readAllBytes(Paths.get("C:\\Users\\anaguilar\\IdeaProjects\\DemoProjectRest\\src\\main\\resources\\addPlace.json"))))
                .when().post("/maps/api/place/add/json")
                .then().assertThat()
                    .statusCode(200).body("scope",equalTo("APP"))
                    .header("server","Apache/2.4.18 (Ubuntu)")
                    .extract().response().asPrettyString();
        System.out.println(response);

        //Add place -> Update previous place with new address -> Get place to validate new address updated
        JsonPath js = new JsonPath(response);
        String placeId=js.getString("place_id");
        System.out.println(placeId);

        //Update
        String newAddress = "Mulsay, USA";
        given().log().all().queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeId+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n")
                .when().put("/maps/api/place/update/json")
                .then().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        //Validate changes
        String getPlaceResponse = given().log().all().queryParam("key","qaclick123").queryParam("place_id",placeId)
                .when().get("/maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();
        JsonPath js1 = ReusableMethods.rawToJson(getPlaceResponse);
        String actualAddress = js1.getString("address");

        Assert.assertEquals(actualAddress,newAddress,"Address are not the same");
    }

    @Test
    public static void testAnotherAPI(){
        baseURI = "http://dummy.restapiexample.com";
        String test = given().log().all()
                    .when().get("/api/v1/employee/3")
                    .then().assertThat().statusCode(200).extract().response().asPrettyString();
        System.out.println(test);

        JsonPath js = new JsonPath(test);
        String name = js.getString("data.employee_name");
        System.out.println(name);
    }
}
