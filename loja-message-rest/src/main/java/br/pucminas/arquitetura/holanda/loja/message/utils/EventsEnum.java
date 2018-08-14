package br.pucminas.arquitetura.holanda.loja.message.utils;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventsEnum {
	VENDA_CADASTRADA("venda-cadastrada", ChannelsEnum.HTTP_POST);

	private String event;
	private ChannelsEnum channel;

	public static EventsEnum ofEvent(String event) {
		return Arrays.stream(values()).filter(eventsEnum -> eventsEnum.getEvent().equals(event)).findFirst().orElseThrow(IllegalArgumentException::new);
	}
}
