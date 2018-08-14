package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INTEGRACAO_CABECALHO")
public class IntegracaoCabecalho implements Serializable {

	@Id
	@GeneratedValue
	private Long idIntegracaoCabecalho;
	private String chave;
	private String valor;
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "idIntegracao")
	private Integracao integracao;

	IntegracaoCabecalho(String chave, String valor, Integracao integracao) {
		this(null, chave, valor, integracao);
	}

}
