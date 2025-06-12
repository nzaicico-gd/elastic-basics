package com.griddynamics.searchretraining.elasticbasics;

import com.griddynamics.searchretraining.elasticbasics.service.ArticleService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ElasticBasicsApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/elastic-basics/articles/search";
        articleService.createIndex();
    }

    @Test
    void shouldReturnAllArticlesMatchingEmptyQuery() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": ""
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(19))
                .body("articles", hasSize(10));
    }

    @Test
    void shouldReturnNoArticlesWhenQueryDoesNotMatchAny() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": "wrongword"
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(0))
                .body("articles", hasSize(0));
    }

    @Test
    void shouldReturnArticlesMatchingQuery() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": "guide"
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(14))
                .body("articles", hasSize(10));
    }

    @Test
    void shouldReturnArticlesMatchingQueryWithFilters() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": "guide",
                            "authorFilter": "taylor lee",
                            "genreFilter": "productivity"
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(2))
                .body("articles", hasSize(2))
                .body("articles[0].author", is("Taylor Lee"))
                .body("articles[0].genres", hasItem("Productivity"))
                .body("articles[1].author", is("Taylor Lee"))
                .body("articles[1].genres", hasItem("Productivity"));
    }

    @Test
    void shouldReturnArticlesMatchingQueryByTwoKeywords() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": "perfect refrigerator guide"
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(2))
                .body("articles", hasSize(2));
    }

    @Test
    void shouldReturnArticlesWithFacetsMatchingQuery() {
        given()
                .contentType(JSON)
                .body("""
                        {
                            "textQuery": "guide"
                        }
                        """)
                .post()
                .then()
                .log().body()
                .statusCode(200)
                .body("totalHits", is(14))
                .body("articles", hasSize(10))
                .body("facets.genres", hasSize(5))
                .body("facets.genres[0].value", is("Eco-Living"))
                .body("facets.genres[0].count", is(6))
                .body("facets.genres[1].value", is("Consumer Guide"))
                .body("facets.genres[1].count", is(4))
                .body("facets.genres[2].value", is("DIY"))
                .body("facets.genres[2].count", is(4))
                .body("facets.genres[3].value", is("Lifestyle"))
                .body("facets.genres[3].count", is(4))
                .body("facets.genres[4].value", is("Productivity"))
                .body("facets.genres[4].count", is(4))
                .body("facets.authors", hasSize(5))
                .body("facets.authors[0].value", is("Taylor Lee"))
                .body("facets.authors[0].count", is(3))
                .body("facets.authors[1].value", is("Alex Rivers"))
                .body("facets.authors[1].count", is(2))
                .body("facets.authors[2].value", is("Jamie Green"))
                .body("facets.authors[2].count", is(2))
                .body("facets.authors[3].value", is("Riley Thomas"))
                .body("facets.authors[3].count", is(2))
                .body("facets.authors[4].value", is("Avery Johnson"))
                .body("facets.authors[4].count", is(1));
    }

}
