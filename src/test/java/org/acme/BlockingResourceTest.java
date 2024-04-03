package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class BlockingResourceTest {

    @Test
    void testEndpoint() {
        given()
          .when().get("/blocking")
          .then()
             .statusCode(200)
             .body(containsString("EXAMPLE DOMAIN"));
    }

}