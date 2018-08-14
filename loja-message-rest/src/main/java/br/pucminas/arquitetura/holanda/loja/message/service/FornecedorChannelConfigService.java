package br.pucminas.arquitetura.holanda.loja.message.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventsEnum;
import com.fasterxml.jackson.databind.JsonNode;

public interface FornecedorChannelConfigService {
	JsonNode setup(Fornecedor fornecedor, EventsEnum eventsEnum);
}
