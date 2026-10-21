package com.example.sprintboot.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "viaje")
public class Viaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double inversion;

	private LocalDate fechaCreacion;

	@OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Producto> productos = new ArrayList<>();

	public Viaje() {
	}

	public Viaje(Long id, Double inversion, LocalDate fechaCreacion, List<Producto> productos) {
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

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public void agregarProducto(Producto producto) {
		this.productos.add(producto);
		producto.setViaje(this);
	}

	public void removerProducto(Producto producto) {
		this.productos.remove(producto);
		producto.setViaje(null);
	}
}