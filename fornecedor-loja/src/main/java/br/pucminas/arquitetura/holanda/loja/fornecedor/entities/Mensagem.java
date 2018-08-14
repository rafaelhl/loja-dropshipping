package br.pucminas.arquitetura.holanda.loja.fornecedor.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Mensagem implements Serializable {

	@Id
	@GeneratedValue
	private Long idMensagem;
	private String mensagem;

}
