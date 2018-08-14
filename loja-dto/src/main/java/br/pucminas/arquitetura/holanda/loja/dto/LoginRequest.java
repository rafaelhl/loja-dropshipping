package br.pucminas.arquitetura.holanda.loja.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest implements Serializable {
	private String login;
	private String password;
}
