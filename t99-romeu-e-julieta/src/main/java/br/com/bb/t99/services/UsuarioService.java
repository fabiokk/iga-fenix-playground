package br.com.bb.t99.services;

import br.com.bb.t99.exception.ErrosSistema;
import br.com.bb.t99.persistence.dao.UsuarioDao;
import br.com.bb.t99.persistence.models.Usuario;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;

@Traced
@RequestScoped
public class UsuarioService {
	private static final Integer IDADE_MINIMA_PARA_MAIORIDADE = 18;
	private static final String TAG_TRACE_USUARIO = "usuario";
	private static final String ZONE_UTC = "UTC";

	@Inject
	UsuarioDao dao;

	@Inject
	Tracer configuredTracer;

	void validarUsuario(Usuario usuario) {
		addTracerTag(TAG_TRACE_USUARIO, usuario.getNome());
		if(usuario.getNascimento() == null){
			throw new ErrosSistema.CampoNaoInformado("data nascimento");
		}

		LocalDate dataAtual = LocalDate.now();
		int idade = dataAtual.getYear() - usuario.getNascimento().toInstant().atZone(ZoneId.of(ZONE_UTC)).getYear();

		if( idade < IDADE_MINIMA_PARA_MAIORIDADE){
			throw new ErrosSistema.ValidacaoIdadeUsuario(IDADE_MINIMA_PARA_MAIORIDADE, idade);
		}
	}

	void addTracerTag(String nomeTag, String valorTag){
		if(configuredTracer != null && configuredTracer.activeSpan() != null) {
			configuredTracer.activeSpan().setTag(nomeTag, valorTag);
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public int inserirUsuario(Usuario usuario) {
		validarUsuario(usuario);
		return dao.inserirUsuario(usuario);
	}

	@Transactional(rollbackOn = Exception.class)
	public int atualizarUsuario(long id, Usuario usuario) {
		usuario.setId(id);
		validarUsuario(usuario);
		return dao.atualizarUsuario(usuario);
	}

	@Transactional(rollbackOn = Exception.class)
	public int excluirUsuario(Long id)  {
		// Exemplo de regra para lancar exceção
		if(id == 1){
			throw new ErrosSistema.ExclusaoUsuario(id);
		}

		return dao.excluirUsuario(id);
	}
}
