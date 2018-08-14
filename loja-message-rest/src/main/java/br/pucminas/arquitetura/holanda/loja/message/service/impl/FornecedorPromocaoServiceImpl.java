package br.pucminas.arquitetura.holanda.loja.message.service.impl;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.message.service.FornecedorPromocaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FornecedorPromocaoServiceImpl implements FornecedorPromocaoService {

	@Value("${app.loja-api.url}")
	private String apiUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public void enviarPromocao(Usuario usuario, Long idProduto, Long idFornecedor, Double desconto) {
		Promocao promocao = new Promocao();
		promocao.setPromocaoPK(new FornecedorProduto.FornecedorProdutoPK(idProduto, idFornecedor));
		promocao.setDesconto(desconto);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, usuario.getHeaderAuthorization());
		this.restTemplate.exchange(this.apiUrl + "/fornecedores/{idFornecedor}/produtos/{idProduto}/promocoes", HttpMethod.POST, new HttpEntity<>(promocao, headers), Promocao.class, idFornecedor, idProduto);
	}

}
