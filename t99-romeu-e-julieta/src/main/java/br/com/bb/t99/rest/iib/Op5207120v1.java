package br.com.bb.t99.rest.iib;

import br.com.bb.t99.operacao.cadastrarClienteV1.bean.requisicao.DadosRequisicaoCadastrarCliente;
import br.com.bb.t99.operacao.cadastrarClienteV1.bean.resposta.DadosRespostaCadastrarCliente;

import br.com.bb.t99.integration.IntegracaoIIB;
import br.com.bb.t99.integration.InfoIntegracao;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import br.com.bb.dev.tracer.TracedWithRequestID;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/op5207120v1")
@RequestScoped
@TracedWithRequestID
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Tag(name = "Operações IIB", description = "Endpoints para provimento de Operação IIB")
public class Op5207120v1 {

    // Objeto contendo os dados do token infoIntegração contendo os dados do canal que chamou a operação.
    @Inject
    InfoIntegracao infoIntegracao;

    @POST
    @IntegracaoIIB
    @Operation(summary = "CadastrarCliente",
        description = "Descricao da CadastrarCliente alterar a descrição na anotacao @Operation do metodo proverOperacao da classe Op5207120v1.")
    public DadosRespostaCadastrarCliente proverOperacao(DadosRequisicaoCadastrarCliente requisicao) {

        validarEntrada(requisicao);

        var resposta = tratarRequisicao(requisicao);

        return resposta;
    }

    private void validarEntrada(DadosRequisicaoCadastrarCliente requisicao) {
        //TODO Inclua aqui as validações dos campos de entrada da sua operação
        // Em caso de falha lance uma Exception, para isso crie ela na classe ErrosSistema do pacote exception
        // e depois lance ela com o comando abaixo, utilize os exemplos presentes na classe
        // throw new ErrosSistema.SuaExceptionCriada();
    }

    private DadosRespostaCadastrarCliente tratarRequisicao(DadosRequisicaoCadastrarCliente requisicao) {
        var resposta = new DadosRespostaCadastrarCliente();
        //TODO Inclua aqui a logica de negocio da sua operação.
        return resposta;
    }
}

