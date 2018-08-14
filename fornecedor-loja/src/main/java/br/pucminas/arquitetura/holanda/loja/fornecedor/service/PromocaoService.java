package br.pucminas.arquitetura.holanda.loja.fornecedor.service;

import br.pucminas.arquitetura.holanda.loja.fornecedor.entities.PromocaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PromocaoService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${app.loja-message-rest.url:}")
	private String urlLojaMessage;

	public void enviarPromocao(PromocaoDTO promocao) {
		this.restTemplate.exchange(this.urlLojaMessage + "/fornecedor/promocoes", HttpMethod.POST, new HttpEntity<>(promocao), Void.class);
	}

}
