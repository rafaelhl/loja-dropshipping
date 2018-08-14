package br.pucminas.arquitetura.holanda.loja.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ProdutoService extends CrudService<Produto, Long> {
	Page<FornecedorProduto> findAllFornecedores(Long idProduto, Pageable pageable);
}
