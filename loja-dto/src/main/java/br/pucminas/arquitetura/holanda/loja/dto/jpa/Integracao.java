package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@Entity
@Table(name = "INTEGRACAO")
public class Integracao implements Serializable {

	@Id
	@GeneratedValue
	private Long idIntegracao;
	private String url;
	@Lob
	@Type(type = "text")
	private String mensagem;
	@JsonManagedReference
	@OneToMany(mappedBy = "integracao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<IntegracaoCabecalho> cabecalhos = new ArrayList<>();

	public void addCabecalho(String chave, String valor) {
		this.cabecalhos.add(new IntegracaoCabecalho(chave, valor, this));
	}

}
