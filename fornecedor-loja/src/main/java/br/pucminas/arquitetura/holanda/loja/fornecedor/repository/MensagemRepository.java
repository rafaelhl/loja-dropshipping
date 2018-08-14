package br.pucminas.arquitetura.holanda.loja.fornecedor.repository;

import br.pucminas.arquitetura.holanda.loja.fornecedor.entities.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem,Long> {
}
