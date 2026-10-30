package com.example.sprintboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dto.InversionTotalDTO;
import com.example.sprintboot.service.InversionTotalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/inversion-total")
	@Tag(name = "Inversión total", description = "Registro único global de inversión en pesos, en dólares y de la inversión fija editable; los pedidos entregados suman su inversión y los viajes la descuentan")
public class InversionTotalController {

	private final InversionTotalService inversionTotalService;

	public InversionTotalController(InversionTotalService inversionTotalService) {
		this.inversionTotalService = inversionTotalService;
	}

	@GetMapping
	@Operation(summary = "Obtener inversión total", description = "Devuelve la inversión global en pesos, en dólares y la inversión fija (registro único). Si no existe, lo crea con valores en 0.")
	@ApiResponse(responseCode = "200", description = "Inversión total obtenida",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = InversionTotalDTO.class)))
	public InversionTotalDTO obtener() {
		return inversionTotalService.obtener();
	}

	@PutMapping
	@Operation(summary = "Actualizar inversión total", description = "Modifica la inversión en pesos, en dólares y la inversión fija del registro único. La inversión fija acepta valores negativos.")
	@ApiResponse(responseCode = "200", description = "Inversión total actualizada",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = InversionTotalDTO.class)))
	public InversionTotalDTO actualizar(@RequestBody InversionTotalDTO dto) {
		return inversionTotalService.actualizar(dto);
	}
}