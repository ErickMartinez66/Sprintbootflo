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

import com.example.sprintboot.dto.ArregloDTO;
import com.example.sprintboot.service.ArregloService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/arreglos")
@Tag(name = "Arreglos", description = "Gestión de las cards del catálogo que se muestran en la tienda")
public class ArregloController {

	private final ArregloService arregloService;

	public ArregloController(ArregloService arregloService) {
		this.arregloService = arregloService;
	}

	@GetMapping
	@Operation(summary = "Listar cards", description = "Devuelve todas las cards del catálogo, incluidas las ocultas, ordenadas por el campo orden.")
	@ApiResponse(responseCode = "200", description = "Cards encontradas",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArregloDTO.class)))
	public List<ArregloDTO> listar() {
		return arregloService.listar();
	}

	@GetMapping("/activos")
	@Operation(summary = "Listar cards activas", description = "Devuelve solo las cards visibles en la tienda, ordenadas por el campo orden.")
	@ApiResponse(responseCode = "200", description = "Cards activas encontradas",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArregloDTO.class)))
	public List<ArregloDTO> listarActivos() {
		return arregloService.listarActivos();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener card por id", description = "Devuelve una card según su id.")
	@ApiResponse(responseCode = "200", description = "Card encontrada",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArregloDTO.class)))
	@ApiResponse(responseCode = "404", description = "Card no encontrada")
	public ArregloDTO obtener(@PathVariable Long id) {
		return arregloService.obtenerPorId(id);
	}

	@PostMapping
	@Operation(summary = "Crear card", description = "Crea una card del catálogo. Si no se envía imagenUrl se usa el emoji y el degradado como fallback.")
	@ApiResponse(responseCode = "200", description = "Card creada",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArregloDTO.class)))
	public ArregloDTO crear(@RequestBody ArregloDTO dto) {
		return arregloService.crear(dto);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar card", description = "Actualiza una card existente por su id.")
	@ApiResponse(responseCode = "200", description = "Card actualizada",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArregloDTO.class)))
	@ApiResponse(responseCode = "404", description = "Card no encontrada")
	public ArregloDTO actualizar(@PathVariable Long id, @RequestBody ArregloDTO dto) {
		return arregloService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar card", description = "Elimina permanentemente una card del catálogo.")
	@ApiResponse(responseCode = "204", description = "Card eliminada")
	@ApiResponse(responseCode = "404", description = "Card no encontrada")
	public void eliminar(@PathVariable Long id) {
		arregloService.eliminar(id);
	}
}
