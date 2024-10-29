package com.juaracoding.movies;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RatingTest {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGVjNGFjNTlhYzc5NWY2ZGY2YzFkNGY1MGM0ZmUyMSIsIm5iZiI6MTczMDAwNjkwNC4wNTEyMDIsInN1YiI6IjY3MWExOGI3NGJlMTU0NjllNzBkODM3NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fG29WgAeDFNAVLStu5vEd9hi4IovGzYM_WgiruxHmrw";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    @Test
    public void testPostMovieRating(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("value", 9.0);

        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.header("content-type", "application/json");
        request.body(requestBody.toJSONString());
        Response response = request.post("/movie/1189668/rating");

        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getBody().jsonPath().getBoolean("success"));
        Assert.assertTrue(response.getBody().jsonPath()
                        .getString("status_message").toLowerCase()  // use lower and contains because the message
                        .contains("success"));                           // output depend on it first post rating or not

        // for example, just running the test two times, the message will different, print message below to see it
        System.out.println(response.getBody().jsonPath().getString("status_message"));
    }

    // negative test
    @Test
    public void testWithoutContentType(){
        JSONObject requestBody = new JSONObject();
        requestBody.put("value", 9.0);

        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.body(requestBody.toJSONString());
        Response response = request.post("/movie/1189668/rating");

        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertFalse(response.getBody().jsonPath().getBoolean("success"));
        Assert.assertEquals(
                response.getBody().jsonPath().getString("status_message"),
                "Invalid parameters: Your request parameters are incorrect."
        );
    }

    @Test
    public void testRequestWithoutToken(){

        given()
                .get("/movie/1189668/rating")
                .then()
                .statusCode(401);
    }

    @Test
    public void testWrongUrl(){

        given()
                .header("authorization", token)
                .when()
                .get("/movies/1189668/rating")
                .then()
                .statusCode(404);
    }

}
