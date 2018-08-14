package br.pucminas.arquitetura.holanda.loja.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthenticationResponse implements Serializable {
	private String accessToken;
	private String tokenType = "Bearer";
	private String authorization;

	public JwtAuthenticationResponse(String accessToken, String authorization) {
		this.accessToken = accessToken;
		this.authorization = authorization;
	}

}
