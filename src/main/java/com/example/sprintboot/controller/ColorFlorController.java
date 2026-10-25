package com.example.sprintboot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dto.ColorFlorDTO;
import com.example.sprintboot.service.ColorFlorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/colores")
@Tag(name = "Colores", description = "Gestión de colores disponibles en el formulario de pedido")
public class ColorFlorController {

    private final ColorFlorService colorFlorService;

    public ColorFlorController(ColorFlorService colorFlorService) {
        this.colorFlorService = colorFlorService;
    }

    @GetMapping
    @Operation(summary = "Listar colores", description = "Devuelve todos los colores registrados")
    @ApiResponse(responseCode = "200", description = "Colores encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorFlorDTO.class)))
    public List<ColorFlorDTO> listar() {
        return colorFlorService.listar();
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar colores visibles", description = "Devuelve los colores activos ordenados para el formulario")
    @ApiResponse(responseCode = "200", description = "Colores activos encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorFlorDTO.class)))
    public List<ColorFlorDTO> listarActivos() {
        return colorFlorService.listarActivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener color por id", description = "Devuelve un color según su id")
    @ApiResponse(responseCode = "200", description = "Color encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorFlorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Color no encontrado")
    public ColorFlorDTO obtener(@PathVariable Long id) {
        return colorFlorService.obtenerPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear color", description = "Crea un nuevo color")
    @ApiResponse(responseCode = "200", description = "Color creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorFlorDTO.class)))
    public ColorFlorDTO crear(@RequestBody ColorFlorDTO dto) {
        return colorFlorService.crear(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar color", description = "Actualiza un color existente por su id")
    @ApiResponse(responseCode = "200", description = "Color actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ColorFlorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Color no encontrado")
    public ColorFlorDTO actualizar(@PathVariable Long id, @RequestBody ColorFlorDTO dto) {
        return colorFlorService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar color", description = "Elimina un color")
    @ApiResponse(responseCode = "204", description = "Color eliminado")
    @ApiResponse(responseCode = "404", description = "Color no encontrado")
    public void eliminar(@PathVariable Long id) {
        colorFlorService.eliminar(id);
    }
}
