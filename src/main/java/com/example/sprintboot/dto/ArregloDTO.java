package com.example.sprintboot.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa una card del catálogo mostrada en la tienda.")
public class ArregloDTO {

    @Schema(description = "ID de la card", example = "1")
    private Long id;

    @Schema(description = "Nombre del arreglo", example = "Rosas de Terciopelo")
    private String nombre;

    @Schema(description = "Descripción comercial del arreglo", example = "Clásicas rosas en tonos rosados con lazo de terciopelo.")
    private String descripcion;

    @Schema(description = "Precio de venta del arreglo en pesos", example = "110000.00")
    private Double precioVenta;

    @Schema(description = "Cantidad de flores que incluye", example = "24")
    private Integer cantidadFlores;

    @Schema(description = "Color principal de las flores", example = "rosa")
    private String colorFlores;

    @Schema(description = "Accesorios incluidos en el arreglo")
    private List<AccesorioDTO> accesorios = new ArrayList<>();

    @Schema(description = "Emoji usado como fallback visual cuando no hay imagen", example = "\ud83d\udca5")
    private String emoji;

    @Schema(description = "Degradado CSS usado como fallback visual cuando no hay imagen", example = "linear-gradient(160deg, #f7e2bd, #e5b989)")
    private String gradiente;

    @Schema(description = "URL pública de la imagen de la card. Si va vacía se usa el emoji y el degradado", example = "https://mi-hosting.com/rosas.jpg")
    private String imagenUrl;

    @Schema(description = "Indica si la card se muestra en la tienda", example = "true")
    private Boolean activo;

    @Schema(description = "Orden de aparición en el catálogo (menor primero)", example = "1")
    private Integer orden;

    public ArregloDTO() {
    }

    public ArregloDTO(Long id, String nombre, String descripcion, Double precioVenta, Integer cantidadFlores,
            String colorFlores, List<AccesorioDTO> accesorios, String emoji, String gradiente, String imagenUrl, Boolean activo,
            Integer orden) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.cantidadFlores = cantidadFlores;
        this.colorFlores = colorFlores;
        this.accesorios = accesorios != null ? accesorios : new ArrayList<>();
        this.emoji = emoji;
        this.gradiente = gradiente;
        this.imagenUrl = imagenUrl;
        this.activo = activo;
        this.orden = orden;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getCantidadFlores() {
        return cantidadFlores;
    }

    public void setCantidadFlores(Integer cantidadFlores) {
        this.cantidadFlores = cantidadFlores;
    }

    public String getColorFlores() {
        return colorFlores;
    }

    public void setColorFlores(String colorFlores) {
        this.colorFlores = colorFlores;
    }

    public List<AccesorioDTO> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(List<AccesorioDTO> accesorios) {
        this.accesorios = accesorios != null ? accesorios : new ArrayList<>();
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getGradiente() {
        return gradiente;
    }

    public void setGradiente(String gradiente) {
        this.gradiente = gradiente;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
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
