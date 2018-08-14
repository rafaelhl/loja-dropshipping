package br.pucminas.arquitetura.holanda.loja.repository;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorProdutoRepository extends JpaRepository<FornecedorProduto, FornecedorProduto.FornecedorProdutoPK> {

	void deleteAllByFornecedorIdFornecedor(Long idFornecedor);
	Page<FornecedorProduto> findAllByFornecedorIdFornecedor(Long idFornecedor, Pageable pageable);
	Page<FornecedorProduto> findAllByProdutoIdProduto(Long idProduto, Pageable pageable);
	
}
