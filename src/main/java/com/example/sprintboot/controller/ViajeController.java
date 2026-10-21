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

import com.example.sprintboot.dto.ProductoDTO;
import com.example.sprintboot.dto.ViajeDTO;
import com.example.sprintboot.service.ViajeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/viajes")
@Tag(name = "Viajes", description = "Gestión de viajes de compras y sus productos asociados")
public class ViajeController {

	private final ViajeService viajeService;

	public ViajeController(ViajeService viajeService) {
		this.viajeService = viajeService;
	}

	@GetMapping
	@Operation(summary = "Listar viajes", description = "Devuelve todos los viajes registrados con sus productos.")
	@ApiResponse(responseCode = "200", description = "Viajes encontrados",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViajeDTO.class)))
	public List<ViajeDTO> listar() {
		return viajeService.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener viaje por id", description = "Devuelve un viaje según su id, con sus productos.")
	@ApiResponse(responseCode = "200", description = "Viaje encontrado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViajeDTO.class)))
	@ApiResponse(responseCode = "404", description = "Viaje no encontrado")
	public ViajeDTO obtener(@PathVariable Long id) {
		return viajeService.obtenerPorId(id);
	}

	@PostMapping
	@Operation(summary = "Crear viaje", description = "Crea un viaje. Opcionalmente incluye productos anidados en el body.")
	@ApiResponse(responseCode = "200", description = "Viaje creado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViajeDTO.class)))
	public ViajeDTO crear(@RequestBody ViajeDTO dto) {
		return viajeService.crear(dto);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar viaje", description = "Actualiza la inversión y fecha. Si el body incluye productos, reemplaza la lista completa.")
	@ApiResponse(responseCode = "200", description = "Viaje actualizado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViajeDTO.class)))
	@ApiResponse(responseCode = "404", description = "Viaje no encontrado")
	public ViajeDTO actualizar(@PathVariable Long id, @RequestBody ViajeDTO dto) {
		return viajeService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar viaje", description = "Elimina un viaje y, en cascada, todos sus productos.")
	@ApiResponse(responseCode = "204", description = "Viaje eliminado")
	@ApiResponse(responseCode = "404", description = "Viaje no encontrado")
	public void eliminar(@PathVariable Long id) {
		viajeService.eliminar(id);
	}

	@GetMapping("/{id}/productos")
	@Operation(summary = "Listar productos de un viaje", description = "Devuelve los productos asociados al viaje indicado.")
	@ApiResponse(responseCode = "200", description = "Productos encontrados",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Viaje no encontrado")
	public List<ProductoDTO> productosDelViaje(@PathVariable Long id) {
		return viajeService.listarProductos(id);
	}

	@PostMapping("/{id}/productos")
	@Operation(summary = "Agregar producto a un viaje", description = "Crea un producto y lo asocia al viaje indicado.")
	@ApiResponse(responseCode = "200", description = "Producto creado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Viaje no encontrado")
	public ProductoDTO agregarProducto(@PathVariable Long id, @RequestBody ProductoDTO dto) {
		return viajeService.agregarProducto(id, dto);
	}

	@PutMapping("/productos/{productoId}")
	@Operation(summary = "Actualizar producto", description = "Actualiza los campos de un producto existente.")
	@ApiResponse(responseCode = "200", description = "Producto actualizado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Producto no encontrado")
	public ProductoDTO actualizarProducto(@PathVariable Long productoId, @RequestBody ProductoDTO dto) {
		return viajeService.actualizarProducto(productoId, dto);
	}

	@DeleteMapping("/productos/{productoId}")
	@Operation(summary = "Eliminar producto", description = "Elimina un producto de su viaje.")
	@ApiResponse(responseCode = "204", description = "Producto eliminado")
	@ApiResponse(responseCode = "404", description = "Producto no encontrado")
	public void eliminarProducto(@PathVariable Long productoId) {
		viajeService.eliminarProducto(productoId);
	}
}