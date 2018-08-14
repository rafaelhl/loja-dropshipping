package br.pucminas.arquitetura.holanda.loja.rest;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.service.PessoaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pessoas")
public class PessoaRestController extends AbstractRestController<Pessoa, Long, PessoaService> {
}
