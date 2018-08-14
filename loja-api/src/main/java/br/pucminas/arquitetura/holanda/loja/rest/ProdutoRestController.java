package br.pucminas.arquitetura.holanda.loja.rest;

import br.pucminas.arquitetura.holanda.loja.dto.PagedResponse;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Produto;
import br.pucminas.arquitetura.holanda.loja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "produtos")
public class ProdutoRestController extends AbstractRestController<Produto, Long, ProdutoService> {

	@Autowired
	private PagedResourcesAssembler<FornecedorProduto> pagedResourcesAssembler;

	@GetMapping("/{idProduto}/fornecedores")
	public PagedResponse<FornecedorProduto> findAllFornecedores(@PathVariable Long idProduto, Pageable pageable) {
		return createPagedResponse(this.pagedResourcesAssembler.toResource(this.service.findAllFornecedores(idProduto, pageable)));
	}

}
