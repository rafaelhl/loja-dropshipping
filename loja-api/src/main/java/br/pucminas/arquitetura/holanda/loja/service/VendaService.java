package br.pucminas.arquitetura.holanda.loja.service;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import org.springframework.stereotype.Service;

@Service
public interface VendaService extends CrudService<Venda, Long> {
}
