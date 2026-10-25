package com.example.sprintboot.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final String usuario;

	private final String password;

	private final long horasSesion;

	private final Map<String, Instant> tokens = new ConcurrentHashMap<>();

	public AuthService(
			@Value("${app.admin.usuario:adyeri}") String usuario,
			@Value("${app.admin.password:Ruffus23*}") String password,
			@Value("${app.admin.sesion.horas:8}") long horasSesion) {
		this.usuario = usuario;
		this.password = password;
		this.horasSesion = horasSesion > 0 ? horasSesion : 8;
	}

	public String login(String usuarioIngresado, String passwordIngresada) {
		if (!coincide(usuarioIngresado, usuario) || !coincide(passwordIngresada, password)) {
			return null;
		}
		String token = UUID.randomUUID().toString();
		tokens.put(token, Instant.now().plus(Duration.ofHours(horasSesion)));
		return token;
	}

	public boolean validar(String token) {
		if (token == null || token.isBlank()) {
			return false;
		}
		Instant expiracion = tokens.get(token);
		if (expiracion == null) {
			return false;
		}
		if (expiracion.isBefore(Instant.now())) {
			tokens.remove(token);
			return false;
		}
		return true;
	}

	public void logout(String token) {
		if (token != null) {
			tokens.remove(token);
		}
	}

	public String getUsuario() {
		return usuario;
	}

	public static String extraerToken(String autorizacion) {
		if (autorizacion == null || !autorizacion.startsWith("Bearer ")) {
			return null;
		}
		return autorizacion.substring("Bearer ".length()).trim();
	}

	private boolean coincide(String valor, String esperado) {
		if (valor == null || esperado == null) {
			return false;
		}
		return MessageDigest.isEqual(
				valor.getBytes(StandardCharsets.UTF_8),
				esperado.getBytes(StandardCharsets.UTF_8));
	}
}
