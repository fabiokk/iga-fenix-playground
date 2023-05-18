package br.com.bb.t99.rest;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class RomeuEJulietaResourceTest {

    @Test
    public void testRomeuEJulieta0OuMenorRetorna403() {
        given()
        .when().get("/romeuejulieta/0")
        .then()
        .statusCode(403)
        .body(containsString("O número deve ser positivo") );
    }

    @Test
    public void testRomeuEJulieta1Retorna1() {
        given()
                .when().get("/romeuejulieta/1")
                .then()
                .statusCode(200)
                .body(containsString("1") );
    }

    @Test
    public void testRomeuEJulieta10Retorna10() {
        given()
                .when().get("/romeuejulieta/923")
                .then()
                .statusCode(200)
                .body(containsString("923") );
    }

    @Test
    public void testRomeuEJulieta3RetornaQueijo() {
        given()
                .when().get("/romeuejulieta/3")
                .then()
                .statusCode(200)
                .body(containsString("queijo") );
    }

    @Test
    public void testRomeuEJulieta18RetornaQueijo() {
        given()
                .when().get("/romeuejulieta/18")
                .then()
                .statusCode(200)
                .body(containsString("queijo") );
    }

    @Test
    public void testRomeuEJulieta5RetornaGoiabada() {
        given()
                .when().get("/romeuejulieta/5")
                .then()
                .statusCode(200)
                .body(containsString("goiabada") );
    }
    @Test
    public void testRomeuEJulieta5000RetornaGoiabada() {
        given()
                .when().get("/romeuejulieta/5000")
                .then()
                .statusCode(200)
                .body(containsString("goiabada") );
    }

    @Test
    public void testRomeuEJulieta15RetornaRomeuEJulieta() {
        given()
                .when().get("/romeuejulieta/15")
                .then()
                .statusCode(200)
                .body(containsString("romeu e julieta") );
    }
    @Test
    public void testRomeuEJulieta9000RetornaRomeuEJulieta() {
        given()
                .when().get("/romeuejulieta/9000")
                .then()
                .statusCode(200)
                .body(containsString("romeu e julieta") );
    }

}
