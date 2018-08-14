package br.pucminas.arquitetura.holanda.loja.message.producer;

import java.nio.charset.StandardCharsets;

import br.pucminas.arquitetura.holanda.loja.dto.Notification;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageNotificationProducer {

	public static final String QUEUE_WORK_EXCHANGE = "queue-work-exchange";
	public static final String QUEUE_WORK = "queue-work";

	@Autowired
	private RabbitTemplate template;

	public void sendMessage(Notification notification) {
		notification.setContents(Base64.encodeBase64String(notification.getContents().getBytes(StandardCharsets.UTF_8)));
		this.template.convertAndSend(QUEUE_WORK_EXCHANGE, QUEUE_WORK, notification);
	}
}
