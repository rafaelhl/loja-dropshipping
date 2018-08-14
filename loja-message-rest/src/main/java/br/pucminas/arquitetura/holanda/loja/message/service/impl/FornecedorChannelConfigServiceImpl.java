package br.pucminas.arquitetura.holanda.loja.message.service.impl;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.IntegracaoCabecalho;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.message.service.FornecedorChannelConfigService;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventIdUtils;
import br.pucminas.arquitetura.holanda.loja.message.utils.EventsEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FornecedorChannelConfigServiceImpl implements FornecedorChannelConfigService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RestTemplate template;

	@Value("${app.notification-platform.notification-channel-config.url}")
	private String urlNotificationChannelConfig;

	@Autowired
	private EventIdUtils eventIdUtils;

	@Autowired
	private ObjectMapper mapper;

	@Override
	public JsonNode setup(Fornecedor fornecedor, EventsEnum eventsEnum) {
		String channel = eventsEnum.getChannel().getChannel();
		ObjectSchema jsonSchema = this.template.getForObject(this.urlNotificationChannelConfig + "/configs/{channel}/schemas", ObjectSchema.class, channel);
		ObjectNode configNode = mapper.createObjectNode();
		Pessoa pessoa = fornecedor.getPessoa();
		String id = this.eventIdUtils.generateEventId(eventsEnum, pessoa.getCpfCnpj());
		Integracao integracao = fornecedor.getIntegracao();
		String url = integracao.getUrl();
		Map<String, String> headers = integracao.getCabecalhos().stream().collect(Collectors.toMap(IntegracaoCabecalho::getChave, IntegracaoCabecalho::getValor));
		String content = integracao.getMensagem();
		Iterator<String> argsIterator = Arrays.asList(id, url, content, toJson(headers)).iterator();
		jsonSchema.getProperties().keySet().stream().forEach(key -> configNode.put(key, argsIterator.next()));
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		this.template.exchange(this.urlNotificationChannelConfig + "/configs/{channel}", HttpMethod.POST, new HttpEntity<>(configNode, httpHeaders), Void.class, channel);
		return configNode;
	}

	private String toJson(Object arg) {
		try {
			return this.mapper.writeValueAsString(arg);
		} catch (JsonProcessingException e) {
			this.logger.error("Nao foi possivel converter em JSON {} \n {}", arg, e.getLocalizedMessage());
			throw new IllegalArgumentException();
		}
	}

}
