package com.example.sprintboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "costo_c")
public class CostoC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double palos;

	private Double petalos;

	private Double hoja;

	private Double hojam;

	private Double lazo;

	private Double mariposa;

	private Double cilicona;

	private Double otro;

	@OneToOne
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;

	public CostoC() {
	}

	public CostoC(Long id, Double palos, Double petalos, Double hoja, Double hojam, Double lazo,
			Double mariposa, Double cilicona, Double otro, Pedido pedido) {
		this.id = id;
		this.palos = palos;
		this.petalos = petalos;
		this.hoja = hoja;
		this.hojam = hojam;
		this.lazo = lazo;
		this.mariposa = mariposa;
		this.cilicona = cilicona;
		this.otro = otro;
		this.pedido = pedido;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
}