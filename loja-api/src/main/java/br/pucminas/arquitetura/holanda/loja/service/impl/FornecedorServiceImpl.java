package br.pucminas.arquitetura.holanda.loja.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.notification.config.FornecedorChannelConfig;
import br.pucminas.arquitetura.holanda.loja.repository.FornecedorProdutoRepository;
import br.pucminas.arquitetura.holanda.loja.repository.FornecedorRepository;
import br.pucminas.arquitetura.holanda.loja.repository.IntegracaoRepository;
import br.pucminas.arquitetura.holanda.loja.repository.PromocaoRepository;
import br.pucminas.arquitetura.holanda.loja.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FornecedorServiceImpl extends AbstractCrudService<Fornecedor, Long, FornecedorRepository> implements FornecedorService {

	@Autowired
	private FornecedorProdutoRepository fornecedorProdutoRepository;

	@Autowired
	private PromocaoRepository promocaoRepository;

	@Autowired
	private IntegracaoRepository integracaoRepository;

	@Autowired
	private FornecedorChannelConfig channelConfig;

	@Override
	public Fornecedor save(Usuario usuario, Fornecedor fornecedor) {
		fornecedor.getPessoa().getUsuario().setTipoUsuario(Usuario.TipoUsuario.FORNECEDOR);
		return super.save(usuario, fornecedor);
	}

	@Override
	public Fornecedor findOne(Long idFornecedor) {
		Fornecedor fornecedor = super.findOne(idFornecedor);
		fornecedor.getPessoa().getUsuario().setPassword(null);
		return fornecedor;
	}

	@Transactional
	public List<FornecedorProduto> save(Long idFornecedor, List<FornecedorProduto> produtos) {
		produtos.forEach(fornecedorProduto -> {
			fornecedorProduto.setFornecedor(findOne(idFornecedor));
			this.fornecedorProdutoRepository.save(fornecedorProduto);
		});
		return produtos;
	}

	@Override
	@Transactional
	public List<FornecedorProduto> update(Long idFornecedor, List<FornecedorProduto> produtos) {
		this.fornecedorProdutoRepository.deleteAllByFornecedorIdFornecedor(idFornecedor);
		return save(idFornecedor, produtos);
	}

	@Override
	@Transactional
	public void delete(Long idFornecedor) {
		this.fornecedorProdutoRepository.deleteAllByFornecedorIdFornecedor(idFornecedor);
		super.delete(idFornecedor);
	}

	@Override
	public Page<FornecedorProduto> findAllProdutos(Long idFornecedor, Pageable pageable) {
		return this.fornecedorProdutoRepository.findAllByFornecedorIdFornecedor(idFornecedor, pageable);
	}

	@Override
	public Integracao save(Usuario usuario, Long idFornecedor, Integracao integracao) {
		Fornecedor fornecedor = this.repository.getOne(idFornecedor);
		Optional.ofNullable(fornecedor.getIntegracao()).ifPresent(this.integracaoRepository::delete);
		integracao.setIdIntegracao(null);
		fornecedor.setIntegracao(integracao);
		fornecedor = this.repository.save(fornecedor);
		this.channelConfig.setup(usuario, fornecedor);
		return fornecedor.getIntegracao();
	}

	@Override
	public Promocao save(Long idFornecedor, Long idProduto, Promocao promocao) {
		promocao.setFornecedorProduto(this.fornecedorProdutoRepository.getOne(new FornecedorProduto.FornecedorProdutoPK(idProduto, idFornecedor)));
		return this.promocaoRepository.save(promocao);
	}

	@Override
	public Promocao findPromocao(Long idFornecedor, Long idProduto) {
		return this.promocaoRepository.getOne(new FornecedorProduto.FornecedorProdutoPK(idFornecedor, idProduto));
	}
}
