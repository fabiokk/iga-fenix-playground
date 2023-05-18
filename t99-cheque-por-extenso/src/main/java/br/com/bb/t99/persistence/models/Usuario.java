package br.com.bb.t99.persistence.models;

import br.com.bb.t99.persistence.dto.UsuarioPorEstadoDTO;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.Optional;

@SqlResultSetMapping(
		name = "UsuarioPorEstadoSetMapping",
		classes = @ConstructorResult(
				targetClass = UsuarioPorEstadoDTO.class,
				columns = {
						@ColumnResult(name = "estado", type = String.class),
						@ColumnResult(name = "quantidade", type = Long.class)})
)
@NamedNativeQueries({
		@NamedNativeQuery(name="CONSULTAR_USUARIO_POR_ESTADO", query = "SELECT end.estado as estado, count(end.usuario_id) as quantidade " +
				" from exemplo.USUARIOS user " +
				" inner join exemplo.ENDERECO end on end.usuario_id = user.id  " +
				" group by end.estado ",
				resultSetMapping = "UsuarioPorEstadoSetMapping",
				resultClass = UsuarioPorEstadoDTO.class),
		@NamedNativeQuery(name="CONSULTAR_USUARIO", query = "SELECT id, nome, nascimento from exemplo.USUARIOS", resultClass = Usuario.class),
		@NamedNativeQuery(name="CONSULTAR_USUARIO_ID", query = "SELECT id, nome, nascimento from exemplo.USUARIOS WHERE id = :idUsuario", resultClass = Usuario.class),
		@NamedNativeQuery(name="INSERIR_USUARIO", query = "INSERT INTO exemplo.USUARIOS (id, nome, nascimento) VALUES (:idUsuario, :nomeUsuario, :dataNascimento);"),
		@NamedNativeQuery(name="ATUALIZAR_USUARIO", query = "UPDATE exemplo.USUARIOS SET nome = :nomeUsuario, nascimento = :dataNascimento WHERE id = :idUsuario "),
		@NamedNativeQuery(name="EXCLUIR_USUARIO", query = "DELETE exemplo.USUARIOS WHERE  id = :idUsuario"),
})
@Entity
@Table(name="USUARIOS", schema="exemplo")
public class Usuario {

	public Usuario(long id, String nome, Date nascimento) {
		this.id = id;
		this.nome = nome;
		this.nascimento = copyDate(nascimento);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nome;

	private Date nascimento;

	public Usuario() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getNascimento() {
		return copyDate(this.nascimento);
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = copyDate(nascimento);
	}

	private static Date copyDate(Date nascimento) {
		return Optional
				.ofNullable(nascimento)
				.map(Date::getTime)
				.map(Date::new)
				.orElse(null);
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", nascimento=" + nascimento +
				'}';
	}
}
