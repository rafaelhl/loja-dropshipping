package br.pucminas.arquitetura.holanda.loja.notification.messaging;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class VendaProdutoMessage {

	@Value("${app.loja-message-rest.url}")
	private String url;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private JwtTokenProvider tokenProvider;

	public void notificar(Usuario usuario, Venda venda) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + this.tokenProvider.generateToken(usuario));
		this.restTemplate.exchange(this.url + "/venda", HttpMethod.POST, new HttpEntity<>(venda, headers), Void.class);
	}

}
