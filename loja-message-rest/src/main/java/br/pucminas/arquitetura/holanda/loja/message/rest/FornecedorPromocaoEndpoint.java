package br.pucminas.arquitetura.holanda.loja.message.rest;

import java.io.IOException;
import java.util.Map;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.security.UsuarioAutenticado;
import br.pucminas.arquitetura.holanda.loja.message.service.FornecedorPromocaoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FornecedorPromocaoEndpoint {

	@Autowired
	private FornecedorPromocaoService fornecedorPromocaoService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostMapping("/fornecedor/promocoes")
	public void enviarPromocao(@UsuarioAutenticado Usuario usuario, @RequestBody String dadosPromocaoJson) throws IOException {
		Map<String, String> dadosPromocao = this.objectMapper.readValue(dadosPromocaoJson, new TypeReference<Map<String, String>>() {});
		Long idProduto = Long.valueOf(dadosPromocao.get("idProduto"));
		Long idFornecedor = Long.valueOf(dadosPromocao.get("idFornecedor"));
		Double desconto = Double.valueOf(dadosPromocao.get("desconto"));
		this.fornecedorPromocaoService.enviarPromocao(usuario, idProduto, idFornecedor, desconto);
	}

}
