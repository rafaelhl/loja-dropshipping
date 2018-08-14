package br.pucminas.arquitetura.holanda.loja.message.service.impl;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.pucminas.arquitetura.holanda.loja.dto.Notification;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.VendaProduto;
import br.pucminas.arquitetura.holanda.loja.message.producer.MessageNotificationProducer;
import br.pucminas.arquitetura.holanda.loja.message.service.MessageVendaService;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventIdUtils;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventsEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageVendaProdutoServiceImpl implements MessageVendaService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private EventIdUtils eventIdUtils;

	@Autowired
	private MessageNotificationProducer messageNotificationProducer;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void sendMessage(Venda venda) {
		List<VendaProduto> produtos = venda.getProdutos();
		venda.setProdutos(null);
		produtos.stream().forEach(vendaProduto -> {
			Fornecedor fornecedor = vendaProduto.getProduto().getFornecedor();
			Notification notification = new Notification();
			EventsEnum event = EventsEnum.VENDA_CADASTRADA;
			notification.setEvent(this.eventIdUtils.generateEventId(event, fornecedor.getPessoa().getCpfCnpj()));
			notification.setChannel(event.getChannel().getChannel());
			notification.setContents(getContents(venda, vendaProduto));
			this.messageNotificationProducer.sendMessage(notification);
		});
	}

	private String getContents(Venda venda, VendaProduto vendaProduto) {
		vendaProduto.setDadosVenda(venda);
		vendaProduto.getProduto().getProduto().setImagem(null);
		try {
			return this.objectMapper.writeValueAsString(vendaProduto);
		} catch (JsonProcessingException e) {
			logger.error("Erro processando JSON {} \n {}", vendaProduto, e.getLocalizedMessage());
			return null;
		}
	}

}
