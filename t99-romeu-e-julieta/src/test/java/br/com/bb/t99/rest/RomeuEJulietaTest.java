package br.com.bb.t99.rest;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class RomeuEJulietaTest {

    @Test
    public void testRomeuEJulietaEndpointPassandoNegativo() {
        given()
                .when().get("/romeuejulieta/-1")
                .then()
                .statusCode(403)
                .body(containsString("O número deve ser positivo") );
    }

    @Test
    public void testRomeuEJulietaEndpointPassando0() {
        given()
                .when().get("/romeuejulieta/0")
                .then()
                .statusCode(403)
                .body(containsString("O número deve ser positivo") );
    }

    @Test
    public void testRomeuEJulietaEndpointDivisivelPor3() {
        given()
                .when().get("/romeuejulieta/3")
                .then()
                .statusCode(200)
                .body(containsString("queijo"));
    }

     //Fazendo teste do caso goiabada

     @Test
     public void testRomeuEJulietaEndpointDivisivelPor5() {
         given()
                 .when().get("/romeuejulieta/5")
                 .then()
                 .statusCode(200)
                 .body(containsString("goiabada"));
     }
     
	
	  //falta fazer o rest implementação
	  
	  @Test public void testRomeuEJulietaEndpointDivisivelPor3ePor5() {
		  given()
		  		.when().get("/romeuejulieta/15") .then() .statusCode(200)
		  		.body(containsString("romeu e julieta")); }
	 
	  //Para continuar
      @Test
      public void testRomeuEJulietaEndpointDivisivelParaQualquerOutroNumero() {
          given()
              .when().get("/romeuejulieta/23")
              .then().statusCode(200)
              .body(containsString("23"));
      }
}
