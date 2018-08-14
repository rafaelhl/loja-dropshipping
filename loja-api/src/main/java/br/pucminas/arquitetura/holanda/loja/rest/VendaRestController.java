package br.pucminas.arquitetura.holanda.loja.rest;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.service.VendaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vendas")
public class VendaRestController extends AbstractRestController<Venda, Long, VendaService> {
}
