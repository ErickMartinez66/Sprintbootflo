package com.example.sprintboot.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.ColorFlorDTO;
import com.example.sprintboot.entity.ColorFlor;
import com.example.sprintboot.repository.ColorFlorRepository;

@Service
@Transactional
public class ColorFlorService {

    private static final Pattern HEX_PATTERN = Pattern.compile("^#([0-9a-fA-F]{6})$");

    private final ColorFlorRepository colorFlorRepository;

    public ColorFlorService(ColorFlorRepository colorFlorRepository) {
        this.colorFlorRepository = colorFlorRepository;
    }

    public List<ColorFlorDTO> listar() {
        return colorFlorRepository.findAllByOrderByOrdenAscIdAsc().stream()
                .map(this::convertirADto)
                .toList();
    }

    public List<ColorFlorDTO> listarActivos() {
        return colorFlorRepository.findByActivoTrueOrderByOrdenAscIdAsc().stream()
                .map(this::convertirADto)
                .toList();
    }

    public ColorFlorDTO obtenerPorId(Long id) {
        return convertirADto(buscar(id));
    }

    public ColorFlorDTO crear(ColorFlorDTO dto) {
        String nombre = validarNombre(dto.getNombre());
        if (colorFlorRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe un color con ese nombre");
        }
        String hex = validarHex(dto.getHex());

        ColorFlor color = new ColorFlor();
        color.setNombre(nombre);
        color.setValor(generarValor(nombre));
        color.setHex(hex);
        color.setActivo(dto.getActivo() != null ? dto.getActivo() : Boolean.TRUE);
        color.setOrden(dto.getOrden() != null ? dto.getOrden() : 0);
        return convertirADto(colorFlorRepository.save(color));
    }

    public ColorFlorDTO actualizar(Long id, ColorFlorDTO dto) {
        ColorFlor color = buscar(id);

        String nombre = validarNombre(dto.getNombre());
        if (!nombre.equals(color.getNombre()) && colorFlorRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new IllegalArgumentException("Ya existe un color con ese nombre");
        }
        color.setNombre(nombre);
        color.setHex(validarHex(dto.getHex()));
        color.setActivo(dto.getActivo() != null ? dto.getActivo() : color.getActivo());
        color.setOrden(dto.getOrden() != null ? dto.getOrden() : color.getOrden());
        return convertirADto(colorFlorRepository.save(color));
    }

    public void eliminar(Long id) {
        ColorFlor color = buscar(id);
        colorFlorRepository.delete(color);
    }

    private ColorFlor buscar(Long id) {
        return colorFlorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color no encontrado con id " + id));
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del color es obligatorio");
        }
        return nombre.trim();
    }

    private String validarHex(String hex) {
        if (hex == null || hex.isBlank()) {
            throw new IllegalArgumentException("El color hexadecimal es obligatorio");
        }
        String valor = hex.trim();
        if (!HEX_PATTERN.matcher(valor).matches()) {
            throw new IllegalArgumentException("El color debe tener el formato #rrggbb");
        }
        return valor.toLowerCase(Locale.ROOT);
    }

    private String generarValor(String nombre) {
        String base = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        if (base.isEmpty()) {
            base = "color";
        }
        String valor = base;
        int sufijo = 2;
        while (colorFlorRepository.existsByValor(valor)) {
            valor = base + "-" + sufijo++;
        }
        return valor;
    }

    private ColorFlorDTO convertirADto(ColorFlor color) {
        return new ColorFlorDTO(
                color.getId(),
                color.getValor(),
                color.getNombre(),
                color.getHex(),
                color.getActivo(),
                color.getOrden());
    }
}
