package com.example.sprintboot.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un viaje de compras con sus productos asociados.")
public class ViajeDTO {

	@Schema(description = "ID del viaje", example = "1")
	private Long id;

	@Schema(description = "Inversión total del viaje en pesos (valor manual)", example = "450000.00")
	private Double inversion;

	@Schema(description = "Fecha de creación del viaje en formato ISO-8601", example = "2026-10-05")
	private LocalDate fechaCreacion;

	@Schema(description = "Productos asociados al viaje")
	private List<ProductoDTO> productos = new ArrayList<>();

	public ViajeDTO() {
	}

	public ViajeDTO(Long id, Double inversion, LocalDate fechaCreacion, List<ProductoDTO> productos) {
		this.id = id;
		this.inversion = inversion;
		this.fechaCreacion = fechaCreacion;
		this.productos = productos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getInversion() {
		return inversion;
	}

	public void setInversion(Double inversion) {
		this.inversion = inversion;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDate fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public List<ProductoDTO> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductoDTO> productos) {
		this.productos = productos;
	}
}