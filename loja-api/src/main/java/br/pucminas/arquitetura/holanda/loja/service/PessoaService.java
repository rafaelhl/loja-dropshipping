package br.pucminas.arquitetura.holanda.loja.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import org.springframework.stereotype.Service;

@Service
public interface PessoaService extends CrudService<Pessoa, Long> {
}
