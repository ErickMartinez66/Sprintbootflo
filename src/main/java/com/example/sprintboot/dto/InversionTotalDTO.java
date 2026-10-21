package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa la inversión total global en pesos y en dólares.")
public class InversionTotalDTO {

	@Schema(description = "ID del registro único", example = "1")
	private Long id;

	@Schema(description = "Inversión total en pesos", example = "1000000.00")
	private Double inversion;

	@Schema(description = "Inversión total en dólares", example = "250.00")
	private Double inversionDolares;

	@Schema(description = "Inversión fija editable en pesos (puede ser negativa)", example = "-50000.00")
	private Double inversionFija;

	public InversionTotalDTO() {
	}

	public InversionTotalDTO(Long id, Double inversion, Double inversionDolares) {
		this.id = id;
		this.inversion = inversion;
		this.inversionDolares = inversionDolares;
	}

	public InversionTotalDTO(Long id, Double inversion, Double inversionDolares, Double inversionFija) {
		this.id = id;
		this.inversion = inversion;
		this.inversionDolares = inversionDolares;
		this.inversionFija = inversionFija;
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

	public Double getInversionDolares() {
		return inversionDolares;
	}

	public void setInversionDolares(Double inversionDolares) {
		this.inversionDolares = inversionDolares;
	}

	public Double getInversionFija() {
		return inversionFija;
	}

	public void setInversionFija(Double inversionFija) {
		this.inversionFija = inversionFija;
	}
}