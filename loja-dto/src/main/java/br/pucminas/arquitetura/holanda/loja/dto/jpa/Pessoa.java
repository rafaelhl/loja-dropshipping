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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PESSOA")
public class Pessoa implements Serializable {

	@Id
	@GeneratedValue
	private Long idPessoa;
	@Column(unique = true)
	private String cpfCnpj;
	private String nome;
	private String email;
	private String telefone;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

}
