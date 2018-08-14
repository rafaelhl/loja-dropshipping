package br.pucminas.arquitetura.holanda.loja.rest;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

import br.pucminas.arquitetura.holanda.loja.dto.PagedResponse;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.security.UsuarioAutenticado;
import br.pucminas.arquitetura.holanda.loja.repository.PromocaoRepository;
import br.pucminas.arquitetura.holanda.loja.service.FornecedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("fornecedores")
public class FornecedorRestController extends AbstractRestController<Fornecedor, Long, FornecedorService> {

	@Autowired
	private PagedResourcesAssembler<FornecedorProduto> pagedResourcesAssembler;

	@PostMapping("/{idFornecedor}/produtos")
	public List<FornecedorProduto> save(@PathVariable Long idFornecedor, @RequestBody List<FornecedorProduto> produtos) {
		return this.service.save(idFornecedor, produtos);
	}

	@PutMapping("/{idFornecedor}/produtos")
	public List<FornecedorProduto> update(@PathVariable Long idFornecedor, @RequestBody List<FornecedorProduto> produtos) {
		return this.service.update(idFornecedor, produtos);
	}

	@GetMapping("/{idFornecedor}/produtos")
	public PagedResponse<FornecedorProduto> findAllProdutos(@PathVariable Long idFornecedor, Pageable pageable) {
		return createPagedResponse(this.pagedResourcesAssembler.toResource(this.service.findAllProdutos(idFornecedor, pageable)));
	}

	@PostMapping("/{idFornecedor}/integracoes")
	public Integracao save(@UsuarioAutenticado Usuario usuario, @PathVariable Long idFornecedor, @RequestBody Integracao integracao) {
		return this.service.save(usuario, idFornecedor, integracao);
	}

	@PutMapping("/{idFornecedor}/integracoes")
	public Integracao update(@UsuarioAutenticado Usuario usuario, @PathVariable Long idFornecedor, @RequestBody Integracao integracao) {
		return this.service.save(usuario, idFornecedor, integracao);
	}

	@GetMapping("/{idFornecedor}/integracoes")
	public Integracao findIntegracao(@PathVariable Long idFornecedor) {
		return Optional.ofNullable(this.service.findOne(idFornecedor).getIntegracao()).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/{idFornecedor}/produtos/{idProduto}/promocoes")
	public Promocao save(@PathVariable Long idFornecedor, @PathVariable Long idProduto, @RequestBody Promocao promocao) {
		return this.service.save(idFornecedor, idProduto, promocao);
	}

	@PutMapping("/{idFornecedor}/produtos/{idProduto}/promocoes")
	public Promocao update(@PathVariable Long idFornecedor, @PathVariable Long idProduto, @RequestBody Promocao promocao) {
		return this.service.save(idFornecedor, idProduto, promocao);
	}

	@GetMapping("/{idFornecedor}/produtos/{idProduto}/promocoes")
	public Promocao findPromocao(@PathVariable Long idFornecedor, @PathVariable Long idProduto) {
		return this.service.findPromocao(idFornecedor, idProduto);
	}

}
