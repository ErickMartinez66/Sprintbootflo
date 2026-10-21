package com.example.sprintboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un accesorio disponible para arreglos y pedidos")
public class AccesorioDTO {

    @Schema(description = "ID del accesorio", example = "1")
    private Long id;

    @Schema(description = "Nombre del accesorio", example = "Lazo de terciopelo")
    private String nombre;

    @Schema(description = "Precio del accesorio en pesos", example = "1500.00")
    private Double precio;

    public AccesorioDTO() {
    }

    public AccesorioDTO(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
