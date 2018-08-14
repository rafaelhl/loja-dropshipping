package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "VENDA")
public class Venda implements Serializable {

	@Id
	@GeneratedValue
	private Long idVenda;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVenda;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idVendedor", referencedColumnName = "idPessoa")
	private Pessoa vendedor;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idComprador", referencedColumnName = "idPessoa")
	private Pessoa comprador;
	@JsonManagedReference
	@OneToMany(mappedBy = "venda",  fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<VendaProduto> produtos = new ArrayList<>();

}
