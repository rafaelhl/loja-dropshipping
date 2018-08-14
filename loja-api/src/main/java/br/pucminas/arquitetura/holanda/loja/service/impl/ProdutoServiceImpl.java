package br.pucminas.arquitetura.holanda.loja.service.impl;

import javax.transaction.Transactional;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Produto;
import br.pucminas.arquitetura.holanda.loja.repository.FornecedorProdutoRepository;
import br.pucminas.arquitetura.holanda.loja.repository.ProdutoRepository;
import br.pucminas.arquitetura.holanda.loja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoServiceImpl extends AbstractCrudService<Produto, Long, ProdutoRepository> implements ProdutoService {

	@Autowired
	private FornecedorProdutoRepository fornecedorProdutoRepository;

	@Override
	@Transactional(value = Transactional.TxType.REQUIRES_NEW)
	public Produto findOne(Long aLong) {
		return this.repository.getOne(aLong);
	}

	@Override
	public Page<FornecedorProduto> findAllFornecedores(Long idProduto, Pageable pageable) {
		return this.fornecedorProdutoRepository.findAllByProdutoIdProduto(idProduto, pageable);
	}
}
