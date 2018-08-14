package br.pucminas.arquitetura.holanda.loja.message.rest;

import java.util.Base64;
import javax.servlet.http.HttpServletRequest;

import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Produto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.VendaProduto;
import br.pucminas.arquitetura.holanda.loja.message.LojaMessageRestApp;
import br.pucminas.arquitetura.holanda.loja.message.security.JwtAuthenticationFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaMessageRestApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.autoconfigure.exclude=io.opentracing.contrib.spring.rabbitmq.RabbitMqTracingAutoConfiguration")
public class MessageVendaRestTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@MockBean
	RabbitAdmin rabbitAdmin;

	@SpyBean
	private JwtAuthenticationFilter authenticationFilter;

	@Before
	public void setup() {
		Mockito.doReturn(new Usuario()).when(this.authenticationFilter).getUserByToken(Mockito.any(HttpServletRequest.class));
	}

	@Test
	public void notificarVendaTest() {
		Pessoa comprador = new Pessoa();
		comprador.setCpfCnpj("000.000.000-01");
		comprador.setEmail("comprador@gmail.com");
		comprador.setNome("Comprador");
		comprador.setTelefone("88888888");
		Usuario usuarioComprador = new Usuario();
		String loginComprador = "comprador";
		usuarioComprador.setLogin(loginComprador);
		String senhaComprador = RandomStringUtils.randomAlphabetic(5);
		usuarioComprador.setPassword(senhaComprador);
		usuarioComprador.setTipoUsuario(Usuario.TipoUsuario.COMPRADOR);
		comprador.setUsuario(usuarioComprador);

		Pessoa vendedor = new Pessoa();
		vendedor.setCpfCnpj("000.000.000-02");
		vendedor.setEmail("vendedor@gmail.com");
		vendedor.setNome("Vendedor");
		vendedor.setTelefone("99999999");
		Usuario usuarioVendedor = new Usuario();
		usuarioVendedor.setLogin("vendedor");
		usuarioVendedor.setPassword(RandomStringUtils.randomAlphabetic(5));
		usuarioVendedor.setTipoUsuario(Usuario.TipoUsuario.VENDEDOR);
		vendedor.setUsuario(usuarioVendedor);

		Produto produto = new Produto();
		produto.setNome("Computador");
		produto.setDescricao("MacBook PRO");
		produto.setImagem(Base64.getEncoder().encodeToString(RandomUtils.nextBytes(FileUtils.ONE_KB_BI.intValue())));

		Fornecedor fornecedor = new Fornecedor ();
		Pessoa pessoa = new Pessoa();
		pessoa.setCpfCnpj("12345678");
		pessoa.setEmail("fornecedor@gmail.com");
		pessoa.setNome("Fornecedor");
		Usuario usuario = new Usuario();
		usuario.setLogin("Usuario");
		usuario.setPassword("12345678");
		pessoa.setUsuario(usuario);
		fornecedor.setPessoa(pessoa);
		FornecedorProduto fornecedorProduto = new FornecedorProduto();
		fornecedorProduto.setPreco(50D);
		fornecedorProduto.setProduto(produto);
		fornecedorProduto.setFornecedor(fornecedor);
		Integracao integracao = new Integracao();
		integracao.setUrl("http://localhost:9090");
		integracao.setMensagem("<venda>${venda.idVenda}</venda>");
		integracao.addCabecalho(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(RandomStringUtils.randomAlphabetic(5).getBytes()));
		integracao.addCabecalho(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
		fornecedor.setIntegracao(integracao);

		Venda venda = new Venda();
		VendaProduto vendaProduto = new VendaProduto();
		vendaProduto.setQuantidade(1);
		vendaProduto.setProduto(fornecedorProduto);
		venda.getProdutos().add(vendaProduto);

		this.testRestTemplate.postForObject("/venda", venda, Void.class);
	}

}
