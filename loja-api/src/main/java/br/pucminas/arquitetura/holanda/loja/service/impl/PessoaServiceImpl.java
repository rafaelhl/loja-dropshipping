package br.pucminas.arquitetura.holanda.loja.service.impl;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.repository.PessoaRepository;
import br.pucminas.arquitetura.holanda.loja.service.PessoaService;
import org.springframework.stereotype.Service;

@Service
public class PessoaServiceImpl extends AbstractCrudService<Pessoa, Long, PessoaRepository> implements PessoaService {
	@Override
	public Pessoa findOne(Long idPessoa) {
		Pessoa pessoa = super.findOne(idPessoa);
		pessoa.getUsuario().setPassword(null);
		return pessoa;
	}
}
