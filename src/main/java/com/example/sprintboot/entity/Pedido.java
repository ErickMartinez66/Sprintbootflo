package com.example.sprintboot.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.sprintboot.config.ColoresConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido {

    public static final double COSTO_POR_FLOR = 250.0;

    public static final String ESTADO_PENDIENTE = "pendiente";

    public static final String ESTADO_ENTREGADO = "entregado";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precioVenta;

    private Double precioInversion;

    private Double precioGanancia;

    private Integer cantidadFlores;

    @Convert(converter = ColoresConverter.class)
    @Column(name = "colores")
    private List<DetalleColor> colores = new ArrayList<>();

    private LocalDate fechaEntrega;

    private String estado;

    private Boolean inversionFijaAplicada = false;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "pedido_accesorio",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "accesorio_id"))
    private Set<Accesorio> accesorios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private CostoC costoC;

    public Pedido() {
    }

    public Pedido(Long id, Double precioVenta, Double precioInversion, Double precioGanancia, Integer cantidadFlores,
            List<DetalleColor> colores, LocalDate fechaEntrega, Set<Accesorio> accesorios, Cliente cliente, CostoC costoC) {
        this.id = id;
        this.precioVenta = precioVenta;
        this.precioInversion = precioInversion;
        this.precioGanancia = precioGanancia;
        this.cantidadFlores = cantidadFlores;
        this.colores = colores != null ? colores : new ArrayList<>();
        this.fechaEntrega = fechaEntrega;
        this.accesorios = accesorios != null ? accesorios : new HashSet<>();
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

    public List<DetalleColor> getColores() {
        return colores;
    }

    public void setColores(List<DetalleColor> colores) {
        this.colores = colores != null ? colores : new ArrayList<>();
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getEstado() {
        return estado == null ? ESTADO_ENTREGADO : estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getInversionFijaAplicada() {
        return inversionFijaAplicada;
    }

    public void setInversionFijaAplicada(Boolean inversionFijaAplicada) {
        this.inversionFijaAplicada = inversionFijaAplicada;
    }

    public Set<Accesorio> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(Set<Accesorio> accesorios) {
        this.accesorios = accesorios != null ? accesorios : new HashSet<>();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public CostoC getCostoC() {
        return costoC;
    }

    public void setCostoC(CostoC costoC) {
        this.costoC = costoC;
    }
}
