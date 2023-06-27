package org.basics;

import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParseLibraryAPI {

    public static void main(String[] args) {

        JsonPath js = new JsonPath(payload.coursePrice());

        // Print no. of courses returned by API
        int count = js.getInt("course.size()");
        System.out.println("Total no. of courses : " + count);

        // Print purchase amt.
        int totalAmount =  js.getInt("dashboard.purchaseAmount");
        System.out.println("Purchase amount : " + totalAmount);

        // Print title of 1st course
        String firstTitle = js.getString("course[0].title");
        System.out.println("First title : " + firstTitle);

        // Print all course title and resp price
        for(int i=0; i<count; i++){
            String title = js.getString("course[" + i +"].title");
            int price = js.getInt("course[" + i +"].price");
            System.out.println("Title : "+ title +" and price is : "+ price);
        }

        // Copies sold by RPA
        for(int i=0;i<count;i++){
            String title = js.get("course["+ i +"].title");
            if(title.equalsIgnoreCase("RPA")){
                int noOfCopies = js.getInt("course["+ i +"].copies");
                System.out.println("RPA copies : "+noOfCopies);
                break;
            }
        }
    }
}
