package SpecBuilders;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import serialization.AddPlace;
import serialization.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuildersTest {
    public static void main(String[] args) {
        AddPlace place = new AddPlace();
        place.setAccuracy(50);
        place.setAddress("Nueva Mulsay");
        place.setLanguage("Spanish");
        place.setPhoneNumber("(+91) 983 893 6664");
        place.setWebsite("facebook.com");
        place.setName("IntelliJ Place");
        List<String> types = new ArrayList<>();
        types.add("house");
        types.add("park");
        types.add("store");
        place.setTypes(types);
        Location location = new Location();
        location.setLat(-38.383494);
        location.setLng(33.427362);
        place.setLocation(location);

        RequestSpecification req = new RequestSpecBuilder()
                .setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key","qaclick123")
                .setContentType(ContentType.JSON).build();

        ResponseSpecification res = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON).build();

        RequestSpecification request=given().spec(req).body(place);
        Response response = request.when().post("/maps/api/place/add/json")
                                   .then().spec(res).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);
    }
}
