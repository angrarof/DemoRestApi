package FirstDemo.files;

import FirstDemo.files.Files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

import static io.restassured.RestAssured.*;

public class JiraTest {


    public static void main(String[] args) {
        RestAssured.baseURI="http://localhost:8080";
        SessionFilter session = new SessionFilter();

        //Start session
        String response = given().relaxedHTTPSValidation().header("Content-Type","application/json")
                .body("{ \"username\": \"angrarof\", \"password\": \"Aguilara01\" }").filter(session)
                .when().post("/rest/auth/1/session")
                .then().log().all().extract().response().asString();

        //Add comment to issue
        String comment = "New comment for rest api from automation: "+LocalDateTime.now();
        String addCommentResponse = given().pathParam("issueId","10006").log().all().header("Content-Type","application/json").body("{\n" +
                "    \"body\":\""+comment+"\"\n" +
                "}").filter(session)
                .when().post("/rest/api/2/issue/{issueId}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath js1 = ReusableMethods.rawToJson(addCommentResponse);
        String commentId = js1.getString("id");

        //Add attachment
        given().pathParam("issueId","10006")
                .header("X-Atlassian-Token","no-check").header("Content-Type","multipart/form-data")
                .filter(session).multiPart("file",new File("src/main/resources/file.txt"))
                .when().post("/rest/api/2/issue/{issueId}/attachments")
                .then().log().all().assertThat().statusCode(200);

        //Get issue
        String issueDetails = given().filter(session).pathParam("issueId","10006").queryParam("fields","comment")
                .when().get("/rest/api/2/issue/{issueId}")
                .then().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = ReusableMethods.rawToJson(issueDetails);
        int size = js.get("fields.comment.comments.size()");

        for(int i=0;i<size;i++){
            String id = js.get("fields.comment.comments["+i+"].id");
            if(id.equalsIgnoreCase(commentId)){
                Assert.assertEquals(js.get("fields.comment.comments["+i+"].body"),comment,"Comments are not equal.");
                System.out.println(comment);
                break;
            }
        }
    }
}
