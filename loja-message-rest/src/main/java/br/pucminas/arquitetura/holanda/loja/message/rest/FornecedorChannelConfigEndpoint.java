package br.pucminas.arquitetura.holanda.loja.message.rest;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.message.service.FornecedorChannelConfigService;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventsEnum;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configs")
public class FornecedorChannelConfigEndpoint {

	@Autowired
	private FornecedorChannelConfigService channelConfigService;

	@PostMapping("/{event}/fornecedor")
	public JsonNode configurarChannelFornecedor(@PathVariable String event, @RequestBody Fornecedor fornecedor) {
		return this.channelConfigService.setup(fornecedor, EventsEnum.ofEvent(event));
	}

}
