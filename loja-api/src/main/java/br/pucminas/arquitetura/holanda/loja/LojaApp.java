package br.pucminas.arquitetura.holanda.loja;

import java.util.Arrays;
import java.util.Optional;
import java.util.TimeZone;
import javax.annotation.PostConstruct;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.repository.PessoaRepository;
import br.pucminas.arquitetura.holanda.loja.repository.VendaRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.opentracing.Tracer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = VendaRepository.class)
@EntityScan(basePackageClasses = {
	Venda.class,
	Jsr310JpaConverters.class
})
public class LojaApp {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String... args) {
		SpringApplication.run(LojaApp.class, args);
	}

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper;
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate(Arrays.asList(new MappingJackson2HttpMessageConverter(objectMapper())));
	}

	@Bean
	InitializingBean adminUser(PessoaRepository pessoaRepository, @Value("${app.admin.password:12345678}") String adminPassword) {
		return () -> {
			String loginAdmin = "admin";
			Pessoa admin = pessoaRepository.findByUsuarioLogin(loginAdmin).orElseGet(Pessoa::new);
			admin.setCpfCnpj("000.000.000-07");
			admin.setEmail("admin@gmail.com");
			admin.setNome("Administrador");
			admin.setTelefone("77777777");
			Usuario usuarioAdmin = Optional.ofNullable(admin.getUsuario()).orElseGet(Usuario::new);
			usuarioAdmin.setLogin("admin");
			usuarioAdmin.setPassword(adminPassword);
			usuarioAdmin.setTipoUsuario(Usuario.TipoUsuario.ADMIN);
			admin.setUsuario(usuarioAdmin);
			pessoaRepository.save(admin);
		};
	}

}
