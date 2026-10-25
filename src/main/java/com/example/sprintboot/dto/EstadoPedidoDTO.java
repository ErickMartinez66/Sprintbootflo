package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para cambiar manualmente el estado de un pedido.")
public class EstadoPedidoDTO {

	@Schema(description = "Nuevo estado del pedido", allowableValues = { "pendiente", "entregado" }, example = "entregado")
	@NotBlank(message = "El estado es obligatorio")
	private String estado;

	public EstadoPedidoDTO() {
	}

	public EstadoPedidoDTO(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
