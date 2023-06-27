package org.basics;

import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {

    @Test
    public void sumOfCourses(){
        // verify sum of all courses matches purchases amount
        JsonPath js = new JsonPath(payload.coursePrice());
        int count = js.getInt("course.size()");
        int total = 0;
        for(int i=0; i<count;i++){
            int pricePerItem = js.getInt("course["+ i +"].price");
            int copiesPerItem = js.getInt("course["+ i +"].copies");
            int salePerItem = pricePerItem*copiesPerItem;
            total = total + salePerItem;
        }
        System.out.println("Sum calculated:"+total);

        int purchaseAmountExpected = js.getInt("dashboard.purchaseAmount");
        Assert.assertEquals(total,purchaseAmountExpected);
    }
}
