package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO que representa un correo a enviar.")
public class EmailDTO {

	@Schema(description = "Correo del destinatario", example = "cliente@correo.com")
	@NotBlank(message = "El destinatario es obligatorio")
	@Email(message = "El destinatario debe ser un correo válido")
	private String destinatario;

	public EmailDTO() {
	}

	public EmailDTO(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
}