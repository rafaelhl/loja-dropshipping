package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "FORNECEDOR_PRODUTO")
public class FornecedorProduto implements Serializable {

	@EmbeddedId
	private FornecedorProdutoPK fornecedorProdutoPK = new FornecedorProdutoPK();
	private double preco;
	@MapsId("idFornecedor")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idFornecedor")
	private Fornecedor fornecedor;
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("idProduto")
	@JoinColumn(name = "idProduto")
	private Produto produto;
	@JsonManagedReference
	@OneToOne(mappedBy = "fornecedorProduto", fetch = FetchType.EAGER)
	private Promocao promocao;

	public double getPreco() {
		return Optional.ofNullable(getPromocao()).filter(Promocao::isAtiva).map(Promocao::getDesconto).map(desconto -> this.preco - desconto).orElse(this.preco);
	}

	@Getter
	@Setter
	@Embeddable
	@NoArgsConstructor
	@AllArgsConstructor
	@EqualsAndHashCode
	public static class FornecedorProdutoPK implements Serializable {
		@Column(insertable = false, updatable = false)
		private Long idProduto;
		@Column(insertable = false, updatable = false)
		private Long idFornecedor;
	}

}
