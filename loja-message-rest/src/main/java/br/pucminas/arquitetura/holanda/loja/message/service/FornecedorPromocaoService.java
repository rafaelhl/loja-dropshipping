package br.pucminas.arquitetura.holanda.loja.message.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;

public interface FornecedorPromocaoService {

	void enviarPromocao(Usuario usuario, Long idProduto, Long idFornecedor, Double desconto);

}
