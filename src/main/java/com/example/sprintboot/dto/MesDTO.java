package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa el resumen mensual de pedidos (ganancia y cantidad).")
public class MesDTO {

	@Schema(description = "ID del mes que codifica anio y mes (anio*100 + mes)", example = "202609")
	private Long id;

	@Schema(description = "Mes del 1 al 12", example = "9")
	private Integer mes;

	@Schema(description = "Año", example = "2026")
	private Integer anio;

	@Schema(description = "Suma de las ganancias de los pedidos entregados en el mes", example = "8500000.00")
	private Double gananciaTotal;

	@Schema(description = "Cantidad de pedidos entregados en el mes", example = "12")
	private Integer cantidadPedidos;

	public MesDTO() {
	}

	public MesDTO(Long id, Integer mes, Integer anio, Double gananciaTotal, Integer cantidadPedidos) {
		this.id = id;
		this.mes = mes;
		this.anio = anio;
		this.gananciaTotal = gananciaTotal;
		this.cantidadPedidos = cantidadPedidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Double getGananciaTotal() {
		return gananciaTotal;
	}

	public void setGananciaTotal(Double gananciaTotal) {
		this.gananciaTotal = gananciaTotal;
	}

	public Integer getCantidadPedidos() {
		return cantidadPedidos;
	}

	public void setCantidadPedidos(Integer cantidadPedidos) {
		this.cantidadPedidos = cantidadPedidos;
	}
}