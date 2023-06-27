package jira;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTest_Session_ValidateResponseBasedOnPreviousResponses {

    @Test
    public void testJira() {

        RestAssured.baseURI = "http://localhost:8080";

        //login to JIRA
        SessionFilter session = new SessionFilter();

        // relaxedHTTPSValidation bypass the http certification when proper certificate is not available
        given().relaxedHTTPSValidation().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"username\": \"monal.haribhakti\",\n" +
                        "    \"password\": \"moviesnow\"\n" +
                        "}")
                .log().all()
                .filter(session)
                .when().post("/rest/auth/1/session")
                .then().extract().response().asString();

        String message = "Hey, how are you?";

        //response format is displayed in file jiraLoginResponse.json file
        // create issue response - jiraCreateIssueResponse.json



        // Add comment
        // add comment response format - jiraAddCommentResponse.json file
        String addCommentResponse = given().pathParam("id", "10004").log().all()
                .body("{\n" +
                        "    \"body\": \""+message+"\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                .header("Content-Type", "application/json")
                .filter(session)
                .when().post("/rest/api/2/issue/{id}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        JsonPath jsComment = new JsonPath(addCommentResponse);
        String commentId = jsComment.get("id");

        // ATTACH file in request
        given().header("X-Atlassian-Token","no-check").filter(session).pathParam("id","10004")
                .header("Content-Type","multipart/form-data")
                .multiPart("file", new File("D:\\Monal\\APIProject\\src\\main\\resources\\jira.text"))
                .when().post("/rest/api/2/issue/{id}/attachments")
                .then().log().all().assertThat().statusCode(200);

        // GET issue Details
        String issueDetails = given().filter(session).pathParam("id", "10004").log().all()
                .queryParam("fields", "comment")
                .when().get("/rest/api/2/issue/{id}")
                .then().log().all().extract().response().asString();


        // validate value in body
        // previous response gives all comments of the issue, we need to validate a particular comment text
        // refer file - jiraGetAllComment.json to know the response format
        JsonPath js = new JsonPath(issueDetails);
        int commentsCount = js.getInt("fields.comment.comments.size()");
        for (int i = 0; i < commentsCount; i++) {
            String currentCommentId = js.get("fields.comment.comments[" + i + "].id").toString();
            if (currentCommentId.equalsIgnoreCase(commentId)) {
                String getCommentText = js.get("fields.comment.comments[" + i + "].body").toString();
                System.out.println(getCommentText);
                Assert.assertEquals(getCommentText, message);
            }
        }
    }

}
