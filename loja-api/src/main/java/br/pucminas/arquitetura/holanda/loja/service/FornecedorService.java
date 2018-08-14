package br.pucminas.arquitetura.holanda.loja.service;

import java.util.List;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface FornecedorService extends CrudService<Fornecedor, Long> {
	List<FornecedorProduto> save(Long idFornecedor, List<FornecedorProduto> produtos);
	List<FornecedorProduto> update(Long idFornecedor, List<FornecedorProduto> produtos);
	Page<FornecedorProduto> findAllProdutos(Long idFornecedor, Pageable pageable);
	Integracao save(Usuario usuario, Long idFornecedor, Integracao integracao);
	Promocao save(Long idFornecedor, Long idProduto, Promocao promocao);
	Promocao findPromocao(Long idFornecedor, Long idProduto);
}
