package FirstDemo.files;

import FirstDemo.files.Files.Payload;
import FirstDemo.files.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

public class DynamicJson {

    @Test(dataProvider="BooksData")
    public void AddBook(String isbn, String aisle){
        RestAssured.baseURI="https://rahulshettyacademy.com";
        String response = given().header("Content-Type","application/json")
                .body(Payload.AddBook(isbn,aisle))
                .when().post("/Library/Addbook.php")
                .then().assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReusableMethods.rawToJson(response);
        String id= js.get("ID");
        System.out.println(id);

        //Delete Books
        given().header("Content-Type","application/json")
                .body("{\n" +
                        "\"ID\":\""+id+"\"\n"+
                        "}\n")
                .when().post("/Library/DeleteBook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();
    }

    @DataProvider(name = "BooksData")
    public Object[][] getData(){
        return new Object[][] {{"njnjnsjnjnjnjnj","2222111"},{"asdasdasdasdsad","1111111"}};
    }
}
