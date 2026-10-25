package com.example.sprintboot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "color_flor")
public class ColorFlor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String valor;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, length = 7)
    private String hex;

    private Boolean activo = Boolean.TRUE;

    private Integer orden = 0;

    public ColorFlor() {
    }

    public ColorFlor(Long id, String valor, String nombre, String hex, Boolean activo, Integer orden) {
        this.id = id;
        this.valor = valor;
        this.nombre = nombre;
        this.hex = hex;
        this.activo = activo;
        this.orden = orden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
