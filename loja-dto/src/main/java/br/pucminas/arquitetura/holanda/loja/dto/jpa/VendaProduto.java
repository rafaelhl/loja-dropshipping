package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "VENDA_PRODUTO")
public class VendaProduto implements Serializable {

	@EmbeddedId
	private VendaProdutoPK vendaProdutoPK = new VendaProdutoPK();
	private int quantidade;
	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("fornecedorProdutoPK")
	@JoinColumns({
		@JoinColumn(name = "idFornecedor"),
		@JoinColumn(name = "idProduto")
	})
	private FornecedorProduto produto;
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idVenda")
	@JsonBackReference
	@JoinColumn(name = "idVenda")
	private Venda venda;

	@Transient
	private Venda dadosVenda;

	@Getter
	@Setter
	@Embeddable
	@EqualsAndHashCode
	public static class VendaProdutoPK implements Serializable {
		@Column(insertable = false, updatable = false)
		private Long idVenda;
		@Column(insertable = false, updatable = false)
		private FornecedorProduto.FornecedorProdutoPK fornecedorProdutoPK = new FornecedorProduto.FornecedorProdutoPK();
	}

}
