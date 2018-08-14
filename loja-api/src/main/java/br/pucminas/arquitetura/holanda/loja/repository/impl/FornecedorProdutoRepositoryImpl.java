package br.pucminas.arquitetura.holanda.loja.repository.impl;

import javax.persistence.EntityManager;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class FornecedorProdutoRepositoryImpl extends SimpleJpaRepository<FornecedorProduto, FornecedorProduto.FornecedorProdutoPK> {

	private EntityManager entityManager;

	public FornecedorProdutoRepositoryImpl(EntityManager entityManager) {
		super(FornecedorProduto.class, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public FornecedorProduto save(FornecedorProduto fornecedorProduto) {
		fornecedorProduto.setProduto(this.entityManager.merge(fornecedorProduto.getProduto()));
		this.entityManager.persist(fornecedorProduto);
		return fornecedorProduto;
	}

}
