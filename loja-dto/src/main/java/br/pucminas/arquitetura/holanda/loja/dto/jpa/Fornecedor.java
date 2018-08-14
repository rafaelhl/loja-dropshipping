package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FORNECEDOR")
public class Fornecedor implements Serializable {

	@Id
	@GeneratedValue
	private Long idFornecedor;
	@JoinColumn(name = "idPessoa")
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Pessoa pessoa;
	@JoinColumn(name = "idIntegracao")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Integracao integracao;

}
