package com.example.sprintboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mes")
public class Mes {

	@Id
	private Long id;

	private Integer mes;

	private Integer anio;

	private Double gananciaTotal;

	private Integer cantidadPedidos;

	public Mes() {
	}

	public Mes(Long id, Integer mes, Integer anio, Double gananciaTotal, Integer cantidadPedidos) {
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