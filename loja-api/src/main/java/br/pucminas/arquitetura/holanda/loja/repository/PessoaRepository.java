package br.pucminas.arquitetura.holanda.loja.repository;

import java.util.Optional;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	Pessoa findByUsuarioIdUsuario(Long idUsuario);
	Optional<Pessoa> findByUsuarioLogin(String login);
	Optional<Pessoa> findByCpfCnpj(String cpfCnpj);

}
