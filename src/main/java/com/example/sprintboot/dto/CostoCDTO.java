package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa el costo de creación de un arreglo, con desglose de materiales.")
public class CostoCDTO {

	@Schema(description = "ID del costo de creación", example = "1")
	private Long id;

	@Schema(description = "ID del pedido al que pertenece", example = "1")
	private Long pedidoId;

	@Schema(description = "Valor de los palos (varillas)", example = "25000.00")
	private Double palos;

	@Schema(description = "Valor de los pétalos", example = "40000.00")
	private Double petalos;

	@Schema(description = "Valor de las hojas", example = "15000.00")
	private Double hoja;

	@Schema(description = "Valor de las hojas medianas", example = "12000.00")
	private Double hojam;

	@Schema(description = "Valor del lazo (moño)", example = "8000.00")
	private Double lazo;

	@Schema(description = "Valor de las mariposas decorativas", example = "10000.00")
	private Double mariposa;

	@Schema(description = "Valor de la silicona (pegamento)", example = "5000.00")
	private Double cilicona;

	@Schema(description = "Otros materiales adicionales", example = "0.00")
	private Double otro;

	public CostoCDTO() {
	}

	public CostoCDTO(Long id, Long pedidoId, Double palos, Double petalos, Double hoja, Double hojam,
			Double lazo, Double mariposa, Double cilicona, Double otro) {
		this.id = id;
		this.pedidoId = pedidoId;
		this.palos = palos;
		this.petalos = petalos;
		this.hoja = hoja;
		this.hojam = hojam;
		this.lazo = lazo;
		this.mariposa = mariposa;
		this.cilicona = cilicona;
		this.otro = otro;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public Double getPalos() {
		return palos;
	}

	public void setPalos(Double palos) {
		this.palos = palos;
	}

	public Double getPetalos() {
		return petalos;
	}

	public void setPetalos(Double petalos) {
		this.petalos = petalos;
	}

	public Double getHoja() {
		return hoja;
	}

	public void setHoja(Double hoja) {
		this.hoja = hoja;
	}

	public Double getHojam() {
		return hojam;
	}

	public void setHojam(Double hojam) {
		this.hojam = hojam;
	}

	public Double getLazo() {
		return lazo;
	}

	public void setLazo(Double lazo) {
		this.lazo = lazo;
	}

	public Double getMariposa() {
		return mariposa;
	}

	public void setMariposa(Double mariposa) {
		this.mariposa = mariposa;
	}

	public Double getCilicona() {
		return cilicona;
	}

	public void setCilicona(Double cilicona) {
		this.cilicona = cilicona;
	}

	public Double getOtro() {
		return otro;
	}

	public void setOtro(Double otro) {
		this.otro = otro;
	}
}