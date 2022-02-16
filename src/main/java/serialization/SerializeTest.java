package serialization;

import io.restassured.RestAssured;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializeTest {
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

        RestAssured.baseURI="https://rahulshettyacademy.com";
        String res=given().queryParam("key","qaclick123").body(place)
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response().asString();

        System.out.println(res);
    }
}
