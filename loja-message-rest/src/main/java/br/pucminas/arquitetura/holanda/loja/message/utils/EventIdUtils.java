package br.pucminas.arquitetura.holanda.loja.message.utils;

import org.apache.commons.lang3.RegExUtils;
import org.springframework.stereotype.Component;

@Component
public class EventIdUtils {

	public String generateEventId(EventsEnum event, String subscriberId) {
		return String.format("%s.%s", event.getEvent(), RegExUtils.removeAll(subscriberId, "\\D"));
	}

}
