package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un cliente de la florería.")
public class ClienteDTO {

	@Schema(description = "ID del cliente", example = "1")
	private Long id;

	@Schema(description = "Nombre completo del cliente", example = "Ana María Pérez")
	private String nombre;

	@Schema(description = "Dirección de entrega", example = "Av. Principal #12-34, Barrio Centro")
	private String direccion;

	@Schema(description = "Número de teléfono o celular", example = "310-555-1234")
	private String numero;

	public ClienteDTO() {
	}

	public ClienteDTO(Long id, String nombre, String direccion, String numero) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}