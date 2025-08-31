package br.com.application;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class SubjectControllerTest {
    @Test
    void testFindAllEndpoint() {
        given()
                .when().get("/subjects/all")
                .then()
                .statusCode(200);
//                .body(is("Hello from Quarkus REST"));
    }
}
