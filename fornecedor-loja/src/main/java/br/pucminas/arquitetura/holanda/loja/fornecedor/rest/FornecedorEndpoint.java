package br.pucminas.arquitetura.holanda.loja.fornecedor.rest;

import java.util.List;

import br.pucminas.arquitetura.holanda.loja.fornecedor.entities.Mensagem;
import br.pucminas.arquitetura.holanda.loja.fornecedor.entities.PromocaoDTO;
import br.pucminas.arquitetura.holanda.loja.fornecedor.service.MensagemService;
import br.pucminas.arquitetura.holanda.loja.fornecedor.service.PromocaoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FornecedorEndpoint {

	@Autowired
	private MensagemService mensagemService;

	@Autowired
	private PromocaoService promocaoService;

	@PostMapping("/vendas")
	public void notificarVenda(@RequestBody String mensagem) {
		this.mensagemService.save(mensagem);
	}

	@GetMapping("/vendas")
	public List<Mensagem> getMensagens() {
		return this.mensagemService.getAll();
	}


	@PostMapping("/promocoes")
	public void enviarPromocao(@RequestBody PromocaoDTO promocao) {
		if (StringUtils.isAnyBlank(promocao.getIdProduto(), promocao.getIdFornecedor(), promocao.getLogin(), promocao.getPassword())) {
			throw new IllegalArgumentException("Campos obrigatorios nao informados");
		}
		this.promocaoService.enviarPromocao(promocao);
	}

}
