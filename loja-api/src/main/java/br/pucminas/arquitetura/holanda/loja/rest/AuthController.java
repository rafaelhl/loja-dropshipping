package br.pucminas.arquitetura.holanda.loja.rest;

import br.pucminas.arquitetura.holanda.loja.dto.JwtAuthenticationResponse;
import br.pucminas.arquitetura.holanda.loja.dto.LoginRequest;
import br.pucminas.arquitetura.holanda.loja.dto.jpa.Usuario;
import br.pucminas.arquitetura.holanda.loja.dto.security.UsuarioAutenticado;
import br.pucminas.arquitetura.holanda.loja.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		Authentication authenticate = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
		return ResponseEntity.ok(new JwtAuthenticationResponse(this.tokenProvider.generateToken(authenticate), this.tokenProvider.getAuthorization(authenticate)));
	}

	@GetMapping("/validate")
	public ResponseEntity<?> validate(@UsuarioAutenticado Usuario usuario) {
		return ResponseEntity.ok(usuario);
	}

}
