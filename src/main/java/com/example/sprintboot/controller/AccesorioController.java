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

import com.example.sprintboot.dto.AccesorioDTO;
import com.example.sprintboot.service.AccesorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/accesorios")
@Tag(name = "Accesorios", description = "Gestión de accesorios disponibles")
public class AccesorioController {

    private final AccesorioService accesorioService;

    public AccesorioController(AccesorioService accesorioService) {
        this.accesorioService = accesorioService;
    }

    @GetMapping
    @Operation(summary = "Listar accesorios", description = "Devuelve todos los accesorios registrados")
    @ApiResponse(responseCode = "200", description = "Accesorios encontrados",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccesorioDTO.class)))
    public List<AccesorioDTO> listar() {
        return accesorioService.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener accesorio por id", description = "Devuelve un accesorio según su id")
    @ApiResponse(responseCode = "200", description = "Accesorio encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccesorioDTO.class)))
    @ApiResponse(responseCode = "404", description = "Accesorio no encontrado")
    public AccesorioDTO obtener(@PathVariable Long id) {
        return accesorioService.obtenerPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear accesorio", description = "Crea un nuevo accesorio")
    @ApiResponse(responseCode = "200", description = "Accesorio creado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccesorioDTO.class)))
    public AccesorioDTO crear(@RequestBody AccesorioDTO dto) {
        return accesorioService.crear(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar accesorio", description = "Actualiza un accesorio existente por su id")
    @ApiResponse(responseCode = "200", description = "Accesorio actualizado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccesorioDTO.class)))
    @ApiResponse(responseCode = "404", description = "Accesorio no encontrado")
    public AccesorioDTO actualizar(@PathVariable Long id, @RequestBody AccesorioDTO dto) {
        return accesorioService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar accesorio", description = "Elimina un accesorio")
    @ApiResponse(responseCode = "204", description = "Accesorio eliminado")
    @ApiResponse(responseCode = "404", description = "Accesorio no encontrado")
    public void eliminar(@PathVariable Long id) {
        accesorioService.eliminar(id);
    }
}
