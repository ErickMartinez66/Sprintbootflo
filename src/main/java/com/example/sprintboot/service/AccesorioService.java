package com.example.sprintboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.AccesorioDTO;
import com.example.sprintboot.entity.Accesorio;
import com.example.sprintboot.repository.AccesorioRepository;

@Service
@Transactional
public class AccesorioService {

    private final AccesorioRepository accesorioRepository;

    public AccesorioService(AccesorioRepository accesorioRepository) {
        this.accesorioRepository = accesorioRepository;
    }

    public List<AccesorioDTO> listar() {
        return accesorioRepository.findAll().stream()
                .map(this::convertirADto)
                .toList();
    }

    public AccesorioDTO obtenerPorId(Long id) {
        Accesorio accesorio = buscar(id);
        return convertirADto(accesorio);
    }

    public AccesorioDTO crear(AccesorioDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del accesorio es obligatorio");
        }
        if (accesorioRepository.existsByNombre(dto.getNombre().trim())) {
            throw new IllegalArgumentException("Ya existe un accesorio con ese nombre");
        }
        Accesorio accesorio = new Accesorio();
        accesorio.setNombre(dto.getNombre().trim());
        accesorio.setPrecio(dto.getPrecio());
        return convertirADto(accesorioRepository.save(accesorio));
    }

    public AccesorioDTO actualizar(Long id, AccesorioDTO dto) {
        Accesorio accesorio = buscar(id);
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            String nombreNuevo = dto.getNombre().trim();
            if (!nombreNuevo.equals(accesorio.getNombre()) && accesorioRepository.existsByNombre(nombreNuevo)) {
                throw new IllegalArgumentException("Ya existe un accesorio con ese nombre");
            }
            accesorio.setNombre(nombreNuevo);
        }
        accesorio.setPrecio(dto.getPrecio());
        return convertirADto(accesorioRepository.save(accesorio));
    }

    public void eliminar(Long id) {
        Accesorio accesorio = buscar(id);
        accesorioRepository.delete(accesorio);
    }

    private Accesorio buscar(Long id) {
        return accesorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accesorio no encontrado con id " + id));
    }

    private AccesorioDTO convertirADto(Accesorio accesorio) {
        return new AccesorioDTO(accesorio.getId(), accesorio.getNombre(), accesorio.getPrecio());
    }

    public Accesorio obtenerEntidadPorId(Long id) {
        return buscar(id);
    }
}
