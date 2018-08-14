package br.pucminas.arquitetura.holanda.loja.message.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;

public interface MessageVendaService {
	void sendMessage(Venda venda);
}
