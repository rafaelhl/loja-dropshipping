package br.pucminas.arquitetura.holanda.loja.fornecedor.service;

import java.util.List;

import br.pucminas.arquitetura.holanda.loja.fornecedor.entities.Mensagem;
import br.pucminas.arquitetura.holanda.loja.fornecedor.repository.MensagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensagemService {

	@Autowired
	private MensagemRepository mensagemRepository;

	public void save(String msg) {
		Mensagem mensagem = new Mensagem();
		mensagem.setMensagem(msg);
		this.mensagemRepository.save(mensagem);
	}

	public List<Mensagem> getAll() {
		return this.mensagemRepository.findAll();
	}
}
