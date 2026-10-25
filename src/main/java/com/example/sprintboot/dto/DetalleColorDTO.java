package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa la cantidad de flores de un color específico dentro de un pedido")
public class DetalleColorDTO {

    @Schema(description = "Cantidad de flores de ese color", example = "10")
    private Integer cantidad;

    @Schema(description = "Color tal como lo devuelve el frontend", example = "rosa")
    private String color;

    public DetalleColorDTO() {
    }

    public DetalleColorDTO(Integer cantidad, String color) {
        this.cantidad = cantidad;
        this.color = color;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
