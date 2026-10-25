package com.example.sprintboot.entity;

public class DetalleColor {

    private Integer cantidad;

    private String color;

    public DetalleColor() {
    }

    public DetalleColor(Integer cantidad, String color) {
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
