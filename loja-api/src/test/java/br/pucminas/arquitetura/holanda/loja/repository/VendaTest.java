package br.pucminas.arquitetura.holanda.loja.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import br.pucminas.arquitetura.holanda.loja.LojaApp;
import br.pucminas.arquitetura.holanda.loja.dto.JwtAuthenticationResponse;
import br.pucminas.arquitetura.holanda.loja.dto.LoginRequest;
import br.pucminas.arquitetura.holanda.loja.dto.PagedResponse;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Fornecedor;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.FornecedorProduto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Integracao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Pessoa;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Produto;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Promocao;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Venda;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.VendaProduto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LojaApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VendaTest {

	public static final String USER = "admin";
	public static final String PASSWORD = "12345678";
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	private Pessoa admin;

	@MockBean
	RestTemplate restTemplate;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Before
	public void setup() {
		this.admin = this.pessoaRepository.findByUsuarioLogin(USER).get();
	}

	@Test
	public void saveTest() throws IOException {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setLogin(USER);
		loginRequest.setPassword(PASSWORD);
		JwtAuthenticationResponse authenticationResponse = this.testRestTemplate.postForObject("/auth/login", loginRequest, JwtAuthenticationResponse.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", String.format("%s %s", authenticationResponse.getTokenType(), authenticationResponse.getAccessToken()));

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
		this.testRestTemplate.postForObject("/pessoas", comprador, Pessoa.class);

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
		this.testRestTemplate.postForObject("/pessoas", vendedor, Pessoa.class);

		Produto produto = new Produto();
		produto.setNome("Computador");
		produto.setDescricao("MacBook PRO");
		produto.setImagem(Base64.getEncoder().encodeToString(RandomUtils.nextBytes(FileUtils.ONE_KB_BI.intValue())));
		produto = this.testRestTemplate.exchange("/produtos", HttpMethod.POST, new HttpEntity<>(produto, headers), Produto.class).getBody();

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
		fornecedor = this.testRestTemplate.exchange("/fornecedores", HttpMethod.POST,new HttpEntity<>(fornecedor, headers), Fornecedor.class).getBody();
		FornecedorProduto fornecedorProduto = new FornecedorProduto();
		fornecedorProduto.setPreco(50D);
		fornecedorProduto.setProduto(produto);
		String urlProdutosFornecedor = "/fornecedores/" + fornecedor.getIdFornecedor() + "/produtos";
		this.testRestTemplate.exchange(urlProdutosFornecedor, HttpMethod.POST, new HttpEntity<>(Arrays.asList(fornecedorProduto), headers), String.class);
		String response = this.testRestTemplate.exchange(urlProdutosFornecedor, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();
		PagedResponse<FornecedorProduto> produtos = this.objectMapper.readValue(response, new TypeReference<PagedResponse<FornecedorProduto>>() {});

		Integracao integracao = new Integracao();
		integracao.setUrl("http://localhost:9090");
		integracao.setMensagem("<venda>${venda.idVenda}</venda>");
		integracao.addCabecalho(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(RandomStringUtils.randomAlphabetic(5).getBytes()));
		integracao.addCabecalho(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
		String urlIntegracao = "/fornecedores/" + fornecedor.getIdFornecedor() + "/integracoes";
		this.testRestTemplate.exchange(urlIntegracao, HttpMethod.POST, new HttpEntity<>(integracao, headers), String.class);
		integracao = this.testRestTemplate.exchange(urlIntegracao, HttpMethod.GET, new HttpEntity<>(headers), Integracao.class).getBody();
		Assert.assertNotNull("integração não persistida", integracao);

		Promocao promocao = new Promocao();
		promocao.setDesconto(50.0);
		promocao.setDescricao(RandomStringUtils.randomAlphabetic(50));
		promocao.setImagem(Base64.getEncoder().encodeToString(RandomUtils.nextBytes(FileUtils.ONE_KB_BI.intValue())));
		String urlPromocao = "/fornecedores/" + fornecedor.getIdFornecedor() + "/produtos/" + produto.getIdProduto() + "/promocoes";
		this.testRestTemplate.exchange(urlPromocao, HttpMethod.POST, new HttpEntity<>(promocao, headers), String.class);
		promocao = this.testRestTemplate.exchange(urlPromocao, HttpMethod.GET, new HttpEntity<>(headers), Promocao.class).getBody();
		Assert.assertNotNull("promocao não persistida", promocao);

		Venda venda = new Venda();
		for (Resource<FornecedorProduto> fProduto : produtos.getElements()) {
			VendaProduto vendaProduto = new VendaProduto();
			vendaProduto.setQuantidade(1);
			vendaProduto.setProduto(fProduto.getContent());
			venda.getProdutos().add(vendaProduto);
		}

		loginRequest = new LoginRequest();
		loginRequest.setLogin(loginComprador);
		loginRequest.setPassword(senhaComprador);
		authenticationResponse = this.testRestTemplate.postForObject("/auth/login", loginRequest, JwtAuthenticationResponse.class);
		headers = new HttpHeaders();
		headers.add("Authorization", String.format("%s %s", authenticationResponse.getTokenType(), authenticationResponse.getAccessToken()));

		Venda vendaPersistida = this.testRestTemplate.exchange("/vendas", HttpMethod.POST, new HttpEntity<>(venda, headers), Venda.class).getBody();
		Assert.assertNotNull("venda não persistida", vendaPersistida);
		Assert.assertNotNull("venda sem comprador", vendaPersistida.getComprador());
		Assert.assertNull("venda com vendedor", vendaPersistida.getVendedor());
		Assert.assertTrue("venda sem produtos", CollectionUtils.isNotEmpty(vendaPersistida.getProdutos()));
	}

}
