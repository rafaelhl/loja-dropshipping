package br.pucminas.arquitetura.holanda.loja.service.impl;

import java.io.Serializable;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractCrudService<Entity extends Serializable, ID extends Serializable, Repository extends JpaRepository<Entity, ID>> implements CrudService<Entity, ID> {

	@Autowired
	protected Repository repository;

	@Override
	public Entity save(Usuario usuario, Entity entity) {
		return this.repository.save(entity);
	}

	@Override
	public Page<Entity> findAll(Pageable pageable) {
		return this.repository.findAll(pageable);
	}

	@Override
	public Entity findOne(ID id) {
		return this.repository.getOne(id);
	}

	@Override
	public void delete(ID id) {
		this.repository.deleteById(id);
	}
}
