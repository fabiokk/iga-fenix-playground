package br.com.bb.t99.integration;

import br.com.bb.dev.erros.curio.CurioExceptionMapper;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.bb.arh.operacao.consultarDadosBasicosFuncionarioBBV1.bean.requisicao.DadosRequisicaoConsultarDadosBasicosFuncionarioBB;
import br.com.bb.arh.operacao.consultarDadosBasicosFuncionarioBBV1.bean.resposta.DadosRespostaConsultarDadosBasicosFuncionarioBB;

@ApplicationScoped
@RegisterRestClient(configKey = "curio-host")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@RegisterProvider(CurioExceptionMapper.class)
public interface ConsumidorCurio {

  @POST
  @Path("op252416v1")
  @IntegracaoIIB
  DadosRespostaConsultarDadosBasicosFuncionarioBB executarOp252416v1(
  DadosRequisicaoConsultarDadosBasicosFuncionarioBB requisicao);

}
