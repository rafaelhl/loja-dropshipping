package br.pucminas.arquitetura.holanda.loja.dto;

import java.io.Serializable;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T extends Serializable> {
	private PagedResources.PageMetadata page;
	private Link nextLink;
	private Link previousLink;
	private Collection<Resource<T>> elements;
}
