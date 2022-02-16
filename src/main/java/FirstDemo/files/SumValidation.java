package FirstDemo.files;

import FirstDemo.files.Files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {

    @Test
    public void sumOfCourse(){
        JsonPath js = new JsonPath(Payload.CoursePrice());
        int count = js.getInt("courses.size()");
        System.out.println(count);

        int total=0;
        for(int i=0;i<count;i++){
            int copies = js.get("courses["+i+"].copies");
            int price = js.get("courses["+i+"].price");
            int sum = copies*price;
            total+=sum;
        }
        Assert.assertEquals(total,js.getInt("dashboard.purchaseAmount"),"Actual total is not the same.");
    }
}
