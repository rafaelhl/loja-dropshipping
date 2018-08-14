package br.pucminas.arquitetura.holanda.loja.repository;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntegracaoRepository extends JpaRepository<Integracao, Long> {
}
