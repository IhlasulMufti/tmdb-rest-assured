package com.juaracoding;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class MovieDetailsTest {

    String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1OGVjNGFjNTlhYzc5NWY2ZGY2YzFkNGY1MGM0ZmUyMSIsIm5iZiI6MTczMDAwNjkwNC4wNTEyMDIsInN1YiI6IjY3MWExOGI3NGJlMTU0NjllNzBkODM3NCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.fG29WgAeDFNAVLStu5vEd9hi4IovGzYM_WgiruxHmrw";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    // positive test
    @Test
    public void testGetMovieDetails(){

        given()
                .header("authorization", token)
                .when()
                .get("/movie/1159311")
                .then()
                .statusCode(200)
                .body("id", equalTo(1159311))
                .body("release_date",equalTo("2024-08-02"))
                .body("title",equalTo("My Hero Academia: You're Next"))
                .body("vote_average",equalTo(7.0f));
    }

    // negative test
    @Test
    public void testRequestWithoutToken(){

        given()
                .get("/movie/1159311")
                .then()
                .statusCode(401);
    }

    @Test
    public void testWrongUrl(){

        given()
                .header("authorization", token)
                .when()
                .get("/movies/1159311") //because ID in Url, so it's same test if ID not found
                .then()
                .statusCode(404);
    }
}
