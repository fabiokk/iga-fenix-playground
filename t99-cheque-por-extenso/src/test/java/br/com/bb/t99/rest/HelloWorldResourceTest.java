package br.com.bb.t99.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import javax.ws.rs.core.Response;


@QuarkusTest
public class HelloWorldResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(containsString("Hello!") );
    }

    // @Test
    // public void testdePost() {
    //     given()
    //             .with().body("teste")
    //             .contentType(ContentType.JSON)
    //             .when()
    //             .post("/gsti/atualizarRst")
    //             .then()
    //             .statusCode(Response.Status.NOT_IMPLEMENTED.getStatusCode())
    //             .body(containsString("Implementar Resposta!") );
    // }
}

