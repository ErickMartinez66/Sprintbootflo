package com.example.sprintboot.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precioVenta;

    private Double precioInversion;

    private Double precioGanancia;

    private Integer cantidadFlores;

    private String colorFlores;

    private LocalDate fechaEntrega;

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
            String colorFlores, LocalDate fechaEntrega, Set<Accesorio> accesorios, Cliente cliente, CostoC costoC) {
        this.id = id;
        this.precioVenta = precioVenta;
        this.precioInversion = precioInversion;
        this.precioGanancia = precioGanancia;
        this.cantidadFlores = cantidadFlores;
        this.colorFlores = colorFlores;
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

    public String getColorFlores() {
        return colorFlores;
    }

    public void setColorFlores(String colorFlores) {
        this.colorFlores = colorFlores;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
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
