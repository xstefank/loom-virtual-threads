package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit5.virtual.ShouldNotPin;
import io.quarkus.test.junit5.virtual.VirtualThreadUnit;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
@VirtualThreadUnit
class ProblemsResourceTest {

    @Test
    @ShouldNotPin
    void testEndpoint() {
        given()
          .when().get("/problem/virtual")
          .then()
             .statusCode(200);
    }

    @Test
    @ShouldNotPin
    void testEndpointReactive() {
        given()
            .when().get("/problem/reactive")
            .then()
            .statusCode(200);
    }

}