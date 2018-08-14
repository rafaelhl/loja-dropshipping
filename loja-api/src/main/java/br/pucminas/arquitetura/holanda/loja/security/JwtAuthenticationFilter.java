package br.pucminas.arquitetura.holanda.loja.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String TOKEN_TYPE_BEARER = "Bearer ";
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String headerAuthorization = request.getHeader(HttpHeaders.AUTHORIZATION);
			String jwt = getJwtFromRequest(headerAuthorization);

			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				Long userId = tokenProvider.getUserIdFromJWT(jwt);

				Pessoa pessoa = pessoaRepository.findByUsuarioIdUsuario(userId);
				Usuario usuario = pessoa.getUsuario();
				usuario.setHeaderAuthorization(headerAuthorization);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(String headerAuthorization) {
		if (!StringUtils.hasText(headerAuthorization) || !headerAuthorization.startsWith(TOKEN_TYPE_BEARER)) {
			return null;
		}
		return headerAuthorization.substring(TOKEN_TYPE_BEARER.length());
	}

}
