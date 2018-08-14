package br.pucminas.arquitetura.holanda.loja.fornecedor.entities;

import java.io.Serializable;

import br.pucminas.arquitetura.holanda.loja.dto.LoginRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromocaoDTO extends LoginRequest implements Serializable {

	private String idProduto;
	private String idFornecedor;
	private String desconto;

}
