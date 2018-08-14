package br.pucminas.arquitetura.holanda.loja.repository.impl;

import java.util.Optional;
import javax.persistence.EntityManager;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class VendaRepositoryImpl extends SimpleJpaRepository<Venda, Long> {

	private EntityManager entityManager;

	public VendaRepositoryImpl(EntityManager entityManager) {
		super(Venda.class, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public Venda save(Venda venda) {
		venda.getProdutos().forEach(vendaProduto -> {
			vendaProduto.setVenda(venda);
			FornecedorProduto fornecedorProduto = vendaProduto.getProduto();
			fornecedorProduto.setFornecedor(this.entityManager.merge(fornecedorProduto.getFornecedor()));
			fornecedorProduto.setProduto(this.entityManager.merge(fornecedorProduto.getProduto()));
			vendaProduto.setProduto(this.entityManager.merge(fornecedorProduto));
		});
		venda.setComprador(this.entityManager.merge(venda.getComprador()));
		Optional.ofNullable(venda.getVendedor()).map(this.entityManager::merge).ifPresent(venda::setVendedor);
		this.entityManager.persist(venda);
		return venda;
	}

}
