package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un producto comprado en un viaje.")
public class ProductoDTO {

	@Schema(description = "ID del producto", example = "1")
	private Long id;

	@Schema(description = "ID del viaje al que pertenece", example = "1")
	private Long viajeId;

	@Schema(description = "Nombre del producto", example = "Palos de madera")
	private String nombre;

	@Schema(description = "Cantidad comprada", example = "100")
	private Integer cantidad;

	@Schema(description = "Costo por unidad en pesos", example = "500.00")
	private Double costoPorUnidad;

	public ProductoDTO() {
	}

	public ProductoDTO(Long id, Long viajeId, String nombre, Integer cantidad, Double costoPorUnidad) {
		this.id = id;
		this.viajeId = viajeId;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.costoPorUnidad = costoPorUnidad;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getViajeId() {
		return viajeId;
	}

	public void setViajeId(Long viajeId) {
		this.viajeId = viajeId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Double getCostoPorUnidad() {
		return costoPorUnidad;
	}

	public void setCostoPorUnidad(Double costoPorUnidad) {
		this.costoPorUnidad = costoPorUnidad;
	}
}