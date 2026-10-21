package com.example.sprintboot.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "arreglo")
public class Arreglo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private Double precioVenta;

    private Integer cantidadFlores;

    private String colorFlores;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "arreglo_accesorio",
            joinColumns = @JoinColumn(name = "arreglo_id"),
            inverseJoinColumns = @JoinColumn(name = "accesorio_id"))
    private Set<Accesorio> accesorios = new HashSet<>();

    private String emoji;

    private String gradiente;

    @Column(length = 500)
    private String imagenUrl;

    private Boolean activo = Boolean.TRUE;

    private Integer orden = 0;

    public Arreglo() {
    }

    public Arreglo(Long id, String nombre, String descripcion, Double precioVenta, Integer cantidadFlores,
            String colorFlores, Set<Accesorio> accesorios, String emoji, String gradiente, String imagenUrl, Boolean activo,
            Integer orden) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.cantidadFlores = cantidadFlores;
        this.colorFlores = colorFlores;
        this.accesorios = accesorios != null ? accesorios : new HashSet<>();
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

    public Set<Accesorio> getAccesorios() {
        return accesorios;
    }

    public void setAccesorios(Set<Accesorio> accesorios) {
        this.accesorios = accesorios != null ? accesorios : new HashSet<>();
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
