package br.pucminas.arquitetura.holanda.loja.message.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import br.pucminas.arquitetura.holanda.loja.dto.JwtAuthenticationResponse;
import br.pucminas.arquitetura.holanda.loja.dto.LoginRequest;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import io.undertow.servlet.spec.ServletInputStreamImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Value("${app.loja-api.url}")
	private String apiUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private byte[] requestBody;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			Usuario usuario = getUserByToken(request);
			if (Objects.nonNull(usuario)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}

		HttpServletRequest servletRequest = Optional.ofNullable(this.requestBody)
			.map(body -> (HttpServletRequest) new MyHttpServletRequestWrapper(body, request))
			.orElse(request);
		filterChain.doFilter(servletRequest, response);
	}

	public Usuario getUserByToken(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
			.map(this::getUsuario)
			.orElseGet(() -> getUserByLogin(request));
	}

	private Usuario getUserByLogin(HttpServletRequest request) {
		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			return null;
		}
		LoginRequest loginRequest = readLoginRequest(request);
		JwtAuthenticationResponse authenticationResponse = this.restTemplate.exchange(this.apiUrl + "/auth/login", HttpMethod.POST, new HttpEntity<>(loginRequest), JwtAuthenticationResponse.class).getBody();
		return getUsuario(String.format("%s %s", authenticationResponse.getTokenType(), authenticationResponse.getAccessToken()));
	}

	private LoginRequest readLoginRequest(HttpServletRequest request) {
		try {
			this.requestBody = IOUtils.toByteArray(request.getInputStream());
			return this.objectMapper.readValue(this.requestBody, LoginRequest.class);
		} catch (IOException e) {
			String message = "Invalid Login Requet JSON";
			this.logger.error(message);
			throw new IllegalArgumentException(message);
		}
	}

	private Usuario getUsuario(String headerAuthorization) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, headerAuthorization);
		Usuario usuario = this.restTemplate.exchange(this.apiUrl + "/auth/validate", HttpMethod.GET, new HttpEntity<>(headers), Usuario.class).getBody();
		usuario.setHeaderAuthorization(headerAuthorization);
		return usuario;
	}

	class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
		private byte[] body;

		public MyHttpServletRequestWrapper(byte[] body, HttpServletRequest request) {
			super(request);
			this.body = body;
		}

		@Override
		public ServletInputStream getInputStream() {
			return new DelegatingServletInputStream(new ByteArrayInputStream(body));
		}
	}

	public class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream sourceStream;
		private boolean finished = false;

		/**
		 * Create a DelegatingServletInputStream for the given source stream.
		 *
		 * @param sourceStream the source stream (never {@code null})
		 */
		public DelegatingServletInputStream(InputStream sourceStream) {
			this.sourceStream = sourceStream;
		}

		/**
		 * Return the underlying source stream (never {@code null}).
		 */
		public final InputStream getSourceStream() {
			return this.sourceStream;
		}


		@Override
		public int read() throws IOException {
			int data = this.sourceStream.read();
			if (data == -1) {
				this.finished = true;
			}
			return data;
		}

		@Override
		public int available() throws IOException {
			return this.sourceStream.available();
		}

		@Override
		public void close() throws IOException {
			super.close();
			this.sourceStream.close();
		}

		@Override
		public boolean isFinished() {
			return this.finished;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			throw new UnsupportedOperationException();
		}

	}

}
