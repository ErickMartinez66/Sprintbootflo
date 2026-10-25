package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales para iniciar sesión en el panel de administración.")
public class LoginDTO {

	@Schema(description = "Usuario del administrador", example = "adyeri")
	@NotBlank(message = "El usuario es obligatorio")
	private String usuario;

	@Schema(description = "Contraseña del administrador", example = "********")
	@NotBlank(message = "La contraseña es obligatoria")
	private String password;

	public LoginDTO() {
	}

	public LoginDTO(String usuario, String password) {
		this.usuario = usuario;
		this.password = password;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
