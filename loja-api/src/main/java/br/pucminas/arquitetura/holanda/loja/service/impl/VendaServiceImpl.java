package br.pucminas.arquitetura.holanda.loja.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.VendaProduto;
import br.pucminas.arquitetura.holanda.loja.notification.messaging.VendaProdutoMessage;
import br.pucminas.arquitetura.holanda.loja.repository.FornecedorProdutoRepository;
import br.pucminas.arquitetura.holanda.loja.repository.PessoaRepository;
import br.pucminas.arquitetura.holanda.loja.repository.VendaRepository;
import br.pucminas.arquitetura.holanda.loja.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendaServiceImpl extends AbstractCrudService<Venda, Long, VendaRepository> implements VendaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private FornecedorProdutoRepository fornecedorProdutoRepository;

	@Autowired
	private VendaProdutoMessage notification;

	@Override
	public Venda save(Usuario usuario, Venda venda) {
		venda.setDataVenda(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
		popularPessoa(usuario, venda);
		for (VendaProduto vendaProduto : venda.getProdutos()) {
			vendaProduto.setProduto(this.fornecedorProdutoRepository.getOne(vendaProduto.getProduto().getFornecedorProdutoPK()));
			vendaProduto.setVenda(venda);
		}
		venda = super.save(usuario, venda);
		this.notification.notificar(usuario, venda);
		return venda;
	}

	private void popularPessoa(Usuario usuario, Venda venda) {
		Pessoa pessoa = this.pessoaRepository.findByUsuarioIdUsuario(usuario.getIdUsuario());
		Pessoa comprador = venda.getComprador();
		if (Objects.isNull(comprador)) {
			venda.setComprador(pessoa);
			return;
		}
		this.pessoaRepository.findByCpfCnpj(comprador.getCpfCnpj()).ifPresent(venda::setComprador);
		venda.setVendedor(pessoa);
	}

}
