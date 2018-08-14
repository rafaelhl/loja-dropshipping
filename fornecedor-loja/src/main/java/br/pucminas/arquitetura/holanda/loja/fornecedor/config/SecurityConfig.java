package br.pucminas.arquitetura.holanda.loja.fornecedor.config;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.anyRequest()
					.authenticated()
			.and()
				.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
			.withUser("admin").password(Usuario.PASSWORD_ENCODER.encode("12345678"))
			.authorities(Usuario.TipoUsuario.FORNECEDOR.getAuthority())
		.and().passwordEncoder(Usuario.PASSWORD_ENCODER);
	}

}
