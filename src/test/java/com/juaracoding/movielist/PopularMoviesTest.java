package com.juaracoding.movielist;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class PopularMoviesTest {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGVjNGFjNTlhYzc5NWY2ZGY2YzFkNGY1MGM0ZmUyMSIsIm5iZiI6MTczMDAwNjkwNC4wNTEyMDIsInN1YiI6IjY3MWExOGI3NGJlMTU0NjllNzBkODM3NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fG29WgAeDFNAVLStu5vEd9hi4IovGzYM_WgiruxHmrw";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    @Test
    public void testGetPopularMovies(){
        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.queryParam("language", "en-US");
        request.queryParam("page", "2");
        Response response = request.get("/movie/popular");

        // Validation: status code 200
        Assert.assertEquals(response.getStatusCode(), 200);

        // validation: response have the right movie list
        List<String> actual = response.getBody().jsonPath().getList("results.original_title");
        Assert.assertTrue(actual.contains("SpongeBob Squarepants: Kreepaway Kamp"));

        // validation: every movie have same property (ex original title and movie overview)
        // 20 is the size of list movies API send in one page
        Assert.assertEquals(actual.size(), 20);
        Assert.assertEquals(response.getBody().jsonPath().getList("results.overview").size(), 20);

    }

    // negative test
    @Test
    public void testRequestWithoutToken(){
        RequestSpecification request = RestAssured.given();
        request.queryParam("language", "en-US");
        request.queryParam("page", "2");
        Response response = request.get("/movie/popular");

        // Validation: status code 401
        Assert.assertEquals(response.getStatusCode(), 401);
    }

    @Test
    public void testWrongUrl(){
        RequestSpecification request = RestAssured.given();
        request.header("authorization", token);
        request.queryParam("language", "en-US");
        request.queryParam("page", "2");
        Response response = request.get("/movies/popular");

        // Validation: status code 404
        Assert.assertEquals(response.getStatusCode(), 404);
    }
}
