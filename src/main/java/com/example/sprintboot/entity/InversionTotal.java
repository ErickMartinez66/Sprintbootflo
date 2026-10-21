package com.example.sprintboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inversion_total")
public class InversionTotal {

	public static final long ID_UNICO = 1L;

	@Id
	private Long id;

	private Double inversion;

	private Double inversionDolares;

	private Double inversionFija;

	public InversionTotal() {
	}

	public InversionTotal(Long id, Double inversion, Double inversionDolares) {
		this.id = id;
		this.inversion = inversion;
		this.inversionDolares = inversionDolares;
	}

	public InversionTotal(Long id, Double inversion, Double inversionDolares, Double inversionFija) {
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