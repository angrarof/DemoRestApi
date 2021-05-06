package FirstDemo.files;

import FirstDemo.files.Files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJson {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(Payload.CoursePrice());

        int count = js.getInt("courses.size()");
        System.out.println(count);

        int totalAmount=js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        String titleFirstCourse = js.get("courses[0].title");
        System.out.println(titleFirstCourse);

        for(int i=0; i<count; i++){
            String courseTitle = js.get("courses["+i+"].title");
            int price = js.get("courses["+i+"].price");

            System.out.println("Course Title: "+courseTitle);
            System.out.println("Course Price: "+price);
        }

        for(int i=0;i<count;i++){
            String title = js.get("courses["+i+"].title");
            if(title.equalsIgnoreCase("RPA")){
                int copies = js.get("courses["+i+"].copies");
                System.out.println("Number of copies: "+copies);
                break;
            }
        }



    }

}
