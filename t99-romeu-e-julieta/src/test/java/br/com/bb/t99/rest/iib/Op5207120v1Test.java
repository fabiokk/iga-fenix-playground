package br.com.bb.t99.rest.iib;

import br.com.bb.t99.operacao.cadastrarClienteV1.bean.requisicao.DadosRequisicaoCadastrarCliente;
import br.com.bb.t99.operacao.cadastrarClienteV1.bean.resposta.DadosRespostaCadastrarCliente;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
public class Op5207120v1Test {

	@Test
	public void testOp5207120v1() {

		DadosRequisicaoCadastrarCliente requisicao = new DadosRequisicaoCadastrarCliente();

		//Preencher o objeto de requisicao

		given()
				.contentType(ContentType.JSON)
				.body(requisicao)
				.when()
				.post("/op5207120v1")
				.then()
				// Colocar os campos de resposta com os valores esperados exemplo
				// Nome do Campo: textoDado , Resposta : "Requiscao Entrada - Resposta Processada"
				//.body("textoDado",is("Requiscao Entrada - Resposta Processada"))
				.statusCode(200);
	}
}
