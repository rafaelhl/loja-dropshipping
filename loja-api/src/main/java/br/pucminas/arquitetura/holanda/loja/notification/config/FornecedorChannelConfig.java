package br.pucminas.arquitetura.holanda.loja.notification.config;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
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
public class FornecedorChannelConfig {

	public static final String EVENT_VENDA_CADASTRADA = "venda-cadastrada";
	@Autowired
	private RestTemplate template;

	@Value("${app.loja-message-rest.url}")
	private String url;

	@Autowired
	private JwtTokenProvider tokenProvider;

	public void setup(Usuario usuario, Fornecedor fornecedor) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + this.tokenProvider.generateToken(usuario));
		this.template.exchange(this.url + "/configs/{event}/fornecedor", HttpMethod.POST, new HttpEntity<>(fornecedor, headers), Void.class, EVENT_VENDA_CADASTRADA);
	}

}
