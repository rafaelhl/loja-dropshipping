package br.pucminas.arquitetura.holanda.loja.repository;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, FornecedorProduto.FornecedorProdutoPK> {
}
