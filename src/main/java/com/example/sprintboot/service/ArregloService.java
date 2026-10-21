package com.example.sprintboot.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.AccesorioDTO;
import com.example.sprintboot.dto.ArregloDTO;
import com.example.sprintboot.entity.Accesorio;
import com.example.sprintboot.entity.Arreglo;
import com.example.sprintboot.repository.AccesorioRepository;
import com.example.sprintboot.repository.ArregloRepository;

@Service
@Transactional
public class ArregloService {

    private static final String GRADIENTE_POR_DEFECTO = "linear-gradient(160deg, #faead6, #efbe8f)";

    private final ArregloRepository arregloRepository;
    private final ImagenService imagenService;
    private final AccesorioRepository accesorioRepository;

    public ArregloService(ArregloRepository arregloRepository, ImagenService imagenService,
            AccesorioRepository accesorioRepository) {
        this.arregloRepository = arregloRepository;
        this.imagenService = imagenService;
        this.accesorioRepository = accesorioRepository;
    }

    public List<ArregloDTO> listar() {
        return arregloRepository.findAll(Sort.by("orden", "id")).stream()
                .map(this::convertirADto)
                .toList();
    }

    public List<ArregloDTO> listarActivos() {
        return arregloRepository.findByActivoTrueOrderByOrdenAscIdAsc().stream()
                .map(this::convertirADto)
                .toList();
    }

    public ArregloDTO obtenerPorId(Long id) {
        return convertirADto(buscar(id));
    }

    public ArregloDTO crear(ArregloDTO dto) {
        Arreglo arreglo = new Arreglo();
        aplicarCampos(arreglo, dto);
        return convertirADto(arregloRepository.save(arreglo));
    }

    public ArregloDTO actualizar(Long id, ArregloDTO dto) {
        Arreglo arreglo = buscar(id);
        aplicarCampos(arreglo, dto);
        return convertirADto(arregloRepository.save(arreglo));
    }

    public void eliminar(Long id) {
        Arreglo arreglo = buscar(id);
        String imagenUrl = arreglo.getImagenUrl();
        arregloRepository.delete(arreglo);
        imagenService.eliminarDesdeUrl(imagenUrl);
    }

    private Arreglo buscar(Long id) {
        return arregloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arreglo no encontrado con id " + id));
    }

    private void aplicarCampos(Arreglo arreglo, ArregloDTO dto) {
        arreglo.setNombre(dto.getNombre());
        arreglo.setDescripcion(dto.getDescripcion());
        arreglo.setPrecioVenta(dto.getPrecioVenta());
        arreglo.setCantidadFlores(dto.getCantidadFlores());
        arreglo.setColorFlores(dto.getColorFlores());
        arreglo.setEmoji(dto.getEmoji());
        arreglo.setGradiente(vacio(dto.getGradiente()) ? GRADIENTE_POR_DEFECTO : dto.getGradiente());
        arreglo.setImagenUrl(vacio(dto.getImagenUrl()) ? null : dto.getImagenUrl().trim());
        arreglo.setActivo(dto.getActivo() == null ? Boolean.TRUE : dto.getActivo());
        arreglo.setOrden(dto.getOrden() == null ? 0 : dto.getOrden());

        Set<Accesorio> accesorios = new HashSet<>();
        if (dto.getAccesorios() != null) {
            for (AccesorioDTO adto : dto.getAccesorios()) {
                if (adto != null && adto.getId() != null) {
                    accesorioRepository.findById(adto.getId()).ifPresent(accesorios::add);
                } else if (adto != null && adto.getNombre() != null && !adto.getNombre().isBlank()) {
                    accesorioRepository.findByNombre(adto.getNombre().trim()).ifPresent(accesorios::add);
                }
            }
        }
        arreglo.setAccesorios(accesorios);
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private ArregloDTO convertirADto(Arreglo arreglo) {
        ArregloDTO dto = new ArregloDTO();
        dto.setId(arreglo.getId());
        dto.setNombre(arreglo.getNombre());
        dto.setDescripcion(arreglo.getDescripcion());
        dto.setPrecioVenta(arreglo.getPrecioVenta());
        dto.setCantidadFlores(arreglo.getCantidadFlores());
        dto.setColorFlores(arreglo.getColorFlores());
        dto.setEmoji(arreglo.getEmoji());
        dto.setGradiente(arreglo.getGradiente());
        dto.setImagenUrl(arreglo.getImagenUrl());
        dto.setActivo(arreglo.getActivo() == null ? Boolean.TRUE : arreglo.getActivo());
        dto.setOrden(arreglo.getOrden() == null ? 0 : arreglo.getOrden());

        if (arreglo.getAccesorios() != null) {
            List<AccesorioDTO> lista = arreglo.getAccesorios().stream()
                    .map(acc -> new AccesorioDTO(acc.getId(), acc.getNombre(), acc.getPrecio()))
                    .collect(Collectors.toList());
            dto.setAccesorios(lista);
        }
        return dto;
    }
}
