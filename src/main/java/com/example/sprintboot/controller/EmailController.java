package com.example.sprintboot.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dto.EmailDTO;
import com.example.sprintboot.service.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email")
@Tag(name = "Email", description = "Envío de correos automáticos")
public class EmailController {

	private final EmailService emailService;

	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/enviar")
	@Operation(summary = "Enviar correo de pedido listo",
			description = "Envía automáticamente un correo indicando que el pedido de flores está listo. Solo requiere el destinatario.")
	@ApiResponse(responseCode = "200", description = "Correo enviado",
			content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"mensaje\": \"Correo enviado exitosamente\"}")))
	@ApiResponse(responseCode = "500", description = "Error al enviar el correo")
	public Map<String, String> enviar(@Valid @RequestBody EmailDTO dto) {
		emailService.enviarPedidoListo(dto.getDestinatario());
		return Map.of("mensaje", "Correo enviado exitosamente");
	}
}