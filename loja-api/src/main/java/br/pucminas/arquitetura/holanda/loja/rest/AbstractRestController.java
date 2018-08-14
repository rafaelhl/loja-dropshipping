package br.pucminas.arquitetura.holanda.loja.rest;

import java.io.Serializable;

import br.pucminas.arquitetura.holanda.loja.dto.PagedResponse;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.security.UsuarioAutenticado;
import br.pucminas.arquitetura.holanda.loja.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public abstract class AbstractRestController<Entity extends Serializable, ID extends Serializable, Service extends CrudService<Entity, ID>> {

	@Autowired
	protected Service service;

	@Autowired
	protected PagedResourcesAssembler<Entity> pagedResourcesAssembler;

	@PostMapping
	public Entity save(@UsuarioAutenticado Usuario usuario, @RequestBody Entity entity) {
		return this.service.save(usuario, entity);
	}

	@GetMapping
	public PagedResponse<Entity> findAll(Pageable pageable) {
		return createPagedResponse(this.pagedResourcesAssembler.toResource(this.service.findAll(pageable)));
	}

	@GetMapping("/{id}")
	public Entity read(@PathVariable ID id) {
		return this.service.findOne(id);
	}

	@PutMapping
	public Entity update(@UsuarioAutenticado Usuario usuario, @RequestBody Entity entity) {
		return save(usuario, entity);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable ID id) {
		this.service.delete(id);
	}

	protected <T extends Serializable> PagedResponse<T> createPagedResponse(PagedResources<Resource<T>> pagedResources) {
		return new PagedResponse<>(pagedResources.getMetadata(), pagedResources.getNextLink(), pagedResources.getPreviousLink(), pagedResources.getContent());
	}

}
