package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "PRODUTO")
public class Produto implements Serializable {

	@Id
	@GeneratedValue
	private Long idProduto;
	private String nome;
	private String descricao;
	@Lob
	@Type(type = "text")
	private String imagem;

}
