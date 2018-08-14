package br.pucminas.arquitetura.holanda.loja.message.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChannelsEnum {
	HTTP_POST("http-post");

	private String channel;

}
