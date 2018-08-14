package br.pucminas.arquitetura.holanda.loja.message.rest;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.dto.security.UsuarioAutenticado;
import br.pucminas.arquitetura.holanda.loja.message.service.MessageVendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageVendaEndpoint {

	@Autowired
	private MessageVendaService messageVendaService;

	@PostMapping("/venda")
	public void notificarVenda(@UsuarioAutenticado Usuario usuario, @RequestBody Venda venda) {
		this.messageVendaService.sendMessage(venda);
	}

}
