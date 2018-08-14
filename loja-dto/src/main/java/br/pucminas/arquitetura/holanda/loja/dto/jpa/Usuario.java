package br.pucminas.arquitetura.holanda.loja.dto.jpa;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "USUARIO")
public class Usuario implements Serializable, UserDetails {

	private static final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	public enum TipoUsuario implements GrantedAuthority {
		ADMIN,
		VENDEDOR,
		COMPRADOR,
		FORNECEDOR;

		@Override
		public String getAuthority() {
			return this.name();
		}
	}

	@Id
	@Setter
	@GeneratedValue
	private Long idUsuario;
	@Setter
	@Column(unique = true)
	private String login;
	@Setter
	private String password;

	@Setter
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private TipoUsuario tipoUsuario = TipoUsuario.COMPRADOR;

	private boolean ativo = Boolean.TRUE;

	@Setter
	@Transient
	private String headerAuthorization;

	public void setPassword(String password) {
		this.password = encode(password);
	}

	private String encode(String password) {
		return Optional.ofNullable(password)
			.filter(this::isPasswordNotEncoded)
			.map(Usuario.PASSWORD_ENCODER::encode)
			.orElse(password);
	}

	private boolean isPasswordNotEncoded(String password) {
		return !Usuario.BCRYPT_PATTERN.matcher(password).matches();
	}

	@Override
	@Transient
	@JsonIgnore
	public Collection<GrantedAuthority> getAuthorities() {
		return Arrays.asList(this.tipoUsuario);
	}

	@Override
	@Transient
	@JsonIgnore
	public String getUsername() {
		return getLogin();
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return Boolean.TRUE;
	}

	@Override
	@Transient
	@JsonIgnore
	public boolean isEnabled() {
		return this.ativo;
	}
}
