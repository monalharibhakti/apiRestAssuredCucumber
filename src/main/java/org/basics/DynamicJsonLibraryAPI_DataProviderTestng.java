package org.basics;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class DynamicJsonLibraryAPI_DataProviderTestng {

    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle){
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String response = given().log().all().header("Content-Type","application/json")
                .body(payload.AddBook(isbn,aisle))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String bookId = js.getString("ID");
        System.out.println(bookId);

        // DELETE book
        given().header("Content-Type","application/json")
                .body("{\n" +
                        "    \"ID\": \""+bookId+"\"\n" +
                        "}")
                .when().post("/Library/DeleteBook.php")
                .then().assertThat().statusCode(200);

    }

    @DataProvider(name="BooksData")
    public Object[][] getData(){
       return new Object[][] {
                {"sdkjfh", "878"},
                {"dadd","232"},
                {"adwq","1223"}
        };
    }
}
