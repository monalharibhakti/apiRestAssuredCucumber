package org.basics;

import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CRUDGoogleMaps {
    public static void main(String[] args) throws IOException {

        RestAssured.baseURI = "https://rahulshettyacademy.com";


        // given() = need to provide everything you want to pass in request - params, header, body
        // when() = mention the HTTP method and the URI resource - base is setup in baseURI hence only the next resource path needs to be given in when
        // then() = all the assertions; once request is completed what you want to check

        // POST - add place
        String response = given().log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body(payload.AddPlacePayload())
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .body("scope",equalTo("APP")) // inside response body there's scope key; validate the value of any key with equalTo()
                .header("Server",equalTo("Apache/2.4.41 (Ubuntu)"))
                .extract().response().asString();  // when you want to extract a response as String

        System.out.println(response);

        //jsonPath takes String as input and convert it to Json
        JsonPath jsonPath = new JsonPath(response);

        //getString requires you to give path of the node e.g { location:{ lat="12" } } ; then you need to give location.lat
        String placeid = jsonPath.getString("place_id");

        System.out.println(placeid);


        // PUT - update address
        System.out.println("__________________PUT____________________");
        String address = "70 Walk street";
        given().log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body(payload.UpdatePlacePayload(placeid,address))
                .when().put("/maps/api/place/update/json")
                .then().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));

        System.out.println("__________________PUT COMPLETE___________________");

        // GET - get updated address
        System.out.println("__________________GET____________________");
        String getResponse = given().log().all()
                .queryParam("key","qaclick123")
                .queryParam("place_id",placeid)
                .when().get("/maps/api/place/get/json")
                .then().log().all().statusCode(200).extract().asString();

        JsonPath jsonPath1 = new JsonPath(getResponse);
        String addressFetched = jsonPath1.getString("address");

        Assert.assertEquals(addressFetched,address);


        // Give json file as payload
        // Converts contents of File to Byte -> Convert Byte to String
         given().log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body(new String(Files.readAllBytes(Paths.get("D:\\Monal\\APIProject\\src\\main\\resources\\locationPayload.json"))))
                .when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .body("scope",equalTo("APP")) // inside response body there's scope key; validate the value of any key with equalTo()
                .header("Server",equalTo("Apache/2.4.41 (Ubuntu)"))
                .extract().response().asString();  // when you want to extract a response as String
    }
}