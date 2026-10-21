package com.example.sprintboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto")
public class Producto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;

	private Integer cantidad;

	private Double costoPorUnidad;

	@ManyToOne
	@JoinColumn(name = "viaje_id")
	private Viaje viaje;

	public Producto() {
	}

	public Producto(Long id, String nombre, Integer cantidad, Double costoPorUnidad, Viaje viaje) {
		this.id = id;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.costoPorUnidad = costoPorUnidad;
		this.viaje = viaje;
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

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
}