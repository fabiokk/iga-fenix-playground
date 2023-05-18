package br.com.bb.t99.integration;

import org.eclipse.microprofile.opentracing.Traced;

import javax.interceptor.InterceptorBinding;
import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para utilizar nos metodos que realização ou recebem requisicoes http/rest para utilizar o filtro {@link IntegracaoFilter}
 * para receber e enviar dados de integração no header da requisição.
 * Tambem inclui a anotação para realização do Tracer da aplicação
 */
@Traced
@NameBinding
@InterceptorBinding
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface IntegracaoIIB {}
