package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "PROMOCAO")
public class Promocao implements Serializable {

	@EmbeddedId
	private FornecedorProduto.FornecedorProdutoPK promocaoPK = new FornecedorProduto.FornecedorProdutoPK();
	private String descricao;
	private Double desconto;
	private boolean ativa = true;
	@Lob
	@Type(type = "text")
	private String imagem;
	@JsonBackReference
	@MapsId("idProduto,idFornecedor")
	@OneToOne
	@JoinColumns({
		@JoinColumn(name = "idProduto"),
		@JoinColumn(name = "idFornecedor")
	})
	private FornecedorProduto fornecedorProduto;

}
