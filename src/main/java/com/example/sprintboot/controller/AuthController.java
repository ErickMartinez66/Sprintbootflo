package com.example.sprintboot.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dto.LoginDTO;
import com.example.sprintboot.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Inicio y cierre de sesión del panel de administración")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	@Operation(summary = "Iniciar sesión", description = "Valida las credenciales del administrador y devuelve un token de sesión.")
	@ApiResponse(responseCode = "200", description = "Sesión iniciada")
	@ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
		String token = authService.login(dto.getUsuario(), dto.getPassword());
		if (token == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "Usuario o contraseña incorrectos."));
		}
		return ResponseEntity.ok(Map.of("token", token, "usuario", authService.getUsuario()));
	}

	@PostMapping("/logout")
	@Operation(summary = "Cerrar sesión", description = "Invalida el token de sesión enviado en la cabecera Authorization.")
	@ApiResponse(responseCode = "204", description = "Sesión cerrada")
	public ResponseEntity<Void> logout(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String autorizacion) {
		authService.logout(AuthService.extraerToken(autorizacion));
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/me")
	@Operation(summary = "Sesión actual", description = "Devuelve el usuario de la sesión si el token es válido.")
	@ApiResponse(responseCode = "200", description = "Token válido")
	@ApiResponse(responseCode = "401", description = "Token inválido o expirado")
	public ResponseEntity<?> me(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String autorizacion) {
		String token = AuthService.extraerToken(autorizacion);
		if (!authService.validar(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("message", "No autorizado"));
		}
		return ResponseEntity.ok(Map.of("usuario", authService.getUsuario()));
	}
}
