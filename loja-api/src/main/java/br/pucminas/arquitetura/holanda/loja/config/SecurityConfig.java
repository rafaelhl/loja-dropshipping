package br.pucminas.arquitetura.holanda.loja.config;

import javax.servlet.http.HttpServletResponse;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.repository.PessoaRepository;
import br.pucminas.arquitetura.holanda.loja.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.and()
			.csrf()
				.disable()
			.exceptionHandling()
				.authenticationEntryPoint((request, response, execption) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Não autorizado!"))
				.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
			.authorizeRequests()
				.antMatchers("/auth/login")
					.permitAll()
				.antMatchers(HttpMethod.GET,"/produtos/**", "/fornecedores/**")
					.permitAll()
				.antMatchers(HttpMethod.POST, "/pessoas/**")
					.permitAll()
				.antMatchers(HttpMethod.POST,"/produtos/**", "/fornecedores/**")
					.hasAnyAuthority(Usuario.TipoUsuario.ADMIN.getAuthority(), Usuario.TipoUsuario.FORNECEDOR.getAuthority())
				.antMatchers(HttpMethod.DELETE, "/pessoas/**")
					.hasAuthority(Usuario.TipoUsuario.ADMIN.getAuthority())
			.anyRequest()
				.authenticated();

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService((login) ->
				this.pessoaRepository.findByUsuarioLogin(login).map(Pessoa::getUsuario)
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login)))
			.passwordEncoder(Usuario.PASSWORD_ENCODER);
	}
}
