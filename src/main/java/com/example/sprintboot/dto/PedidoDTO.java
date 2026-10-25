package com.example.sprintboot.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa un pedido de flores, incluyendo el cliente y su costo de creación.")
public class PedidoDTO {

    @Schema(description = "ID del pedido", example = "1")
    private Long id;

    @Schema(description = "Precio de venta en pesos", example = "850000.00")
    private Double precioVenta;

    @Schema(description = "Costo total de inversión (producción) en pesos", example = "420000.00")
    private Double precioInversion;

    @Schema(description = "Ganancia del pedido (precio de venta - inversión) en pesos", example = "430000.00")
    private Double precioGanancia;

    @Schema(description = "Cantidad total de flores del arreglo", example = "24")
    private Integer cantidadFlores;

    @Schema(description = "Cantidades de flores por cada color elegido")
    private List<DetalleColorDTO> colores = new ArrayList<>();

    @Schema(description = "Fecha de entrega en formato ISO-8601", example = "2026-10-01")
    private LocalDate fechaEntrega;

    @Schema(description = "Estado del pedido", allowableValues = { "pendiente", "entregado" }, example = "pendiente")
    private String estado;

    @Schema(description = "Accesorios incluidos")
    private List<AccesorioDTO> accesorios = new ArrayList<>();

    @Schema(description = "Datos del cliente asociado al pedido")
    private ClienteDTO cliente;

    @Schema(description = "Costo de creación (desglose de materiales)")
    private CostoCDTO costoC;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, Double precioVenta, Double precioInversion, Double precioGanancia, Integer cantidadFlores,
            List<DetalleColorDTO> colores, LocalDate fechaEntrega, List<AccesorioDTO> accesorios, ClienteDTO cliente, CostoCDTO costoC) {
        this.id = id;
        this.precioVenta = precioVenta;
        this.precioInversion = precioInversion;
        this.precioGanancia = precioGanancia;
        this.cantidadFlores = cantidadFlores;
        this.colores = colores != null ? colores : new ArrayList<>();
        this.fechaEntrega = fechaEntrega;
        this.accesorios = accesorios != null ? accesorios : new ArrayList<>();
        this.cliente = cliente;
        this.costoC = costoC;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getPrecioInversion() {
        return precioInversion;
    }

    public void setPrecioInversion(Double precioInversion) {
        this.precioInversion = precioInversion;
    }

    public Double getPrecioGanancia() {
        return precioGanancia;
    }

    public void setPrecioGanancia(Double precioGanancia) {
        this.precioGanancia = precioGanancia;
    }

    public Integer getCantidadFlores() {
        return cantidadFlores;
    }

    public void setCantidadFlores(Integer cantidadFlores) {
        this.cantidadFlores = cantidadFlores;
    }

    public List<DetalleColorDTO> getColores() {
        return colores;
    }

    public void setColores(List<DetalleColorDTO> colores) {
        this.colores = colores != null ? colores : new ArrayList<>();
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<AccesorioDTO> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(List<AccesorioDTO> accesorios) {
        this.accesorios = accesorios != null ? accesorios : new ArrayList<>();
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public CostoCDTO getCostoC() {
        return costoC;
    }

    public void setCostoC(CostoCDTO costoC) {
        this.costoC = costoC;
    }
}
