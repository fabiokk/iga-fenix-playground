package br.com.bb.t99.exception;

import br.com.bb.dev.erros.exceptions.BBRuntimeException;
import br.com.bb.dev.erros.model.IncluirNaListaErros;
import java.util.Map;
import org.hibernate.JDBCException;
import br.com.bb.dev.erros.filter.FilterUtils;

@IncluirNaListaErros
public class ErrosSistema {
	/* Abaixo temos alguns exemplos de erros, mas voce pode fazer o construtor de acordo com sua necessidade, mas sempre
	usando o construtor da classe pai (super) mais adequado.
	Para mais exemplos acesse a documentação da lib dev-java-erro em https://fontes.intranet.bb.com.br/dev/dev-java-erro
	 */
	public static final class CampoNaoInformado extends BBRuntimeException {
		public static final String MSG_ERRO = "O campo %s não foi informado corretamente.";
		public static final String COD_ERRO = "100"; // Codigo do erro

		public CampoNaoInformado(String nomeCampo) {
			super(COD_ERRO, String.format(MSG_ERRO, nomeCampo), Map.of("NOME_CAMPO", nomeCampo));
		}
	}

	public static final class ExclusaoUsuario extends BBRuntimeException {
		public static final String MSG_ERRO = "Não foi possivel excluir o usuario.";
		public static final String COD_ERRO = "200"; // Codigo do erro

		public ExclusaoUsuario(long id) {
			super(COD_ERRO, MSG_ERRO);
			this.put("ID_USUARIO", String.valueOf(id));
		}
	}

	public static final class ValidacaoIdadeUsuario extends BBRuntimeException {
		public static final String MSG_ERRO = "O usuario deve ter mais que %d anos.";
		public static final String COD_ERRO = "300"; // Codigo do erro

		public ValidacaoIdadeUsuario(int idadeLimite, int idadeInformada) {
			super(COD_ERRO, String.format(MSG_ERRO, idadeLimite));
			this.putAll(Map.of("IDADE_LIMITE", String.valueOf(idadeLimite),
					"IDADE_INFORMADA", String.valueOf(idadeInformada)));
		}
	}

	public static final class ErroExecucaoSQL extends BBRuntimeException {
		public static final String MSG_ERRO = "Sua Mensagem de erro";
		public static final String COD_ERRO = "900"; // Codigo do erro

		public ErroExecucaoSQL(Throwable e) {
			super(COD_ERRO, MSG_ERRO, e);
			if (e.getCause() instanceof JDBCException) {
				JDBCException jdbcException = (JDBCException) e.getCause();

				if (jdbcException.getSQLException() != null) {
					this.put("SQL_CODE", String.valueOf(jdbcException.getSQLException().getErrorCode()));
				}

				if (jdbcException.getSQL() != null && jdbcException.getSQL().length() > 0 ) {
					this.put("SQL_QUERY", jdbcException.getSQL());
				}

				if (jdbcException.getCause() != null) {
					this.put(FilterUtils.VAR_MOTIVO, jdbcException.getCause().getMessage());
				} else {
					this.put(FilterUtils.VAR_MOTIVO, jdbcException.getMessage());
				}
			}
		}
	}
}

