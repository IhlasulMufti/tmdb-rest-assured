package com.juaracoding;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class NowPlayingTest {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGVjNGFjNTlhYzc5NWY2ZGY2YzFkNGY1MGM0ZmUyMSIsIm5iZiI6MTczMDAwNjkwNC4wNTEyMDIsInN1YiI6IjY3MWExOGI3NGJlMTU0NjllNzBkODM3NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fG29WgAeDFNAVLStu5vEd9hi4IovGzYM_WgiruxHmrw";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    // positive test
    @Test
    public void testGetNowPlayingMovies(){
        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.queryParam("language", "en-US");
        request.queryParam("page", "1");
        Response response = request.get("/movie/now_playing");

        // Validation: status code 200
        Assert.assertEquals(response.getStatusCode(), 200);

        // validation: response have the right movie list
        List<String> actual = response.getBody().jsonPath().getList("results.title");
        Assert.assertTrue(actual.contains("Hellboy: The Crooked Man"));

        // validation: every movie have same property (ex title and release date movie)
        // 20 is the size of list movies API send in one page
        Assert.assertEquals(actual.size(), 20);
        Assert.assertEquals(response.getBody().jsonPath().getList("results.release_date").size(), 20);
    }

    // negative test
    @Test
    public void testRequestWithoutToken(){
        RequestSpecification request = RestAssured.given();
        request.queryParam("language", "en-US");
        request.queryParam("page", "1");
        Response response = request.get("/movie/now_playing");

        // Validation: status code 401
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testWrongUrl(){
        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.queryParam("language", "en-US");
        request.queryParam("page", "1");
        Response response = request.get("/movies/now_playing");

        // Validation: status code 404
        Assert.assertEquals(response.getStatusCode(), 404);
    }

}
