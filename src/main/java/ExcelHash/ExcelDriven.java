package ExcelHash;

import FirstDemo.files.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class ExcelDriven {

    @Test
    public void excelData() throws IOException {
        RestAssured.baseURI="https://rahulshettyacademy.com";
        DataDriven dataDriven = new DataDriven();
        ArrayList<String> bookData = dataDriven.getData("RestAddBook");

        HashMap<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name",bookData.get(1));
        jsonAsMap.put("isbn",bookData.get(2));
        jsonAsMap.put("aisle",bookData.get(3));
        jsonAsMap.put("author",bookData.get(4));

        String response = given().header("Content-Type","application/json").body(jsonAsMap)
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println(id);



    }
}
