package br.pucminas.arquitetura.holanda.loja.service;

import java.io.Serializable;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudService<Entity extends Serializable, ID extends Serializable> {
	Entity save(Usuario usuario, Entity entity);
	Page<Entity> findAll(Pageable pageable);
	Entity findOne(ID id);
	void delete(ID id);
}
