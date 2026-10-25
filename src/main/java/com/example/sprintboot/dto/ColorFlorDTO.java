package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un color disponible en el formulario de pedido")
public class ColorFlorDTO {

    @Schema(description = "ID del color", example = "1")
    private Long id;

    @Schema(description = "Identificador estable del color (no editable)", example = "rosa")
    private String valor;

    @Schema(description = "Nombre visible del color", example = "Rosa")
    private String nombre;

    @Schema(description = "Código hexadecimal del color", example = "#f4a7c3")
    private String hex;

    @Schema(description = "Indica si el color se muestra en el formulario de la tienda", example = "true")
    private Boolean activo;

    @Schema(description = "Orden de aparición (menor primero)", example = "1")
    private Integer orden;

    public ColorFlorDTO() {
    }

    public ColorFlorDTO(Long id, String valor, String nombre, String hex, Boolean activo, Integer orden) {
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
