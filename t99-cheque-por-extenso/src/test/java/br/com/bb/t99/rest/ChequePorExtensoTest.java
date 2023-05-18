package br.com.bb.t99.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import javax.ws.rs.core.Response;


@QuarkusTest
public class ChequePorExtensoTest {

    @Test
    public void testChequeValorErradoMoedaErrada() {

        given()
                .with().body("{\"valor\": 0.0, \"moeda\": \"outro\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/cheque")
                .then()
                .statusCode(403)
                .body(containsString("O número deve ser positivo!"))
                .body(containsString("A moeda precisa real ou dolar") );
    }

    @Test
    public void testChequePassandoValorZero() {

        given()
                .with().body("{\"valor\": 0.0, \"moeda\": \"real\"}")
                .contentType(ContentType.JSON)
                .when()
                .post("/cheque")
                .then()
                .statusCode(403)
                .body(containsString("O número deve ser positivo!"));
    } 

  //TODO
  //Melhorar os testes
     @Test
     public void testChequeUmCentavoReal() {
         given()
                 .with().body("{\"valor\": 0.01, \"moeda\": \"real\"}")
                 .contentType(ContentType.JSON)
                 .when()
                 .post("/cheque")
                 .then()
                 .statusCode(201)
                 .body("message", is("cheque processado com sucesso!"))
                 .body(containsString("um centavo"));
     } 
}
