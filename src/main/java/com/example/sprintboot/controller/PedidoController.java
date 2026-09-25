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

import com.example.sprintboot.dto.ClienteDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos de flores, clientes y costos de creación")
public class PedidoController {

	private final PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}

	@GetMapping
	@Operation(summary = "Listar pedidos", description = "Devuelve todos los pedidos registrados con su cliente y costo de creación.")
	@ApiResponse(responseCode = "200", description = "Pedidos encontrados",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	public List<PedidoDTO> listar() {
		return pedidoService.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener pedido por id", description = "Devuelve un pedido según su id.")
	@ApiResponse(responseCode = "200", description = "Pedido encontrado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Pedido no encontrado")
	public PedidoDTO obtener(@PathVariable Long id) {
		return pedidoService.obtenerPorId(id);
	}

	@PostMapping
	@Operation(summary = "Crear pedido",
			description = "Crea un pedido. Si el body incluye un cliente sin id, se crea el cliente automáticamente y se vincula; si incluye costoC, también se registra el costo de creación.")
	@ApiResponse(responseCode = "200", description = "Pedido creado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	public PedidoDTO crear(@RequestBody PedidoDTO dto) {
		return pedidoService.crear(dto);
	}

	@PostMapping("/{id}/cliente")
	@Operation(summary = "Asignar cliente a un pedido",
			description = "Vincula un cliente (existente por id o nuevo) a un pedido ya creado.")
	@ApiResponse(responseCode = "200", description = "Cliente asignado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	public PedidoDTO asignarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
		return pedidoService.asignarCliente(id, clienteDTO);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualizar pedido", description = "Actualiza los campos de un pedido existente.")
	@ApiResponse(responseCode = "200", description = "Pedido actualizado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	@ApiResponse(responseCode = "404", description = "Pedido no encontrado")
	public PedidoDTO actualizar(@PathVariable Long id, @RequestBody PedidoDTO dto) {
		return pedidoService.actualizar(id, dto);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar pedido", description = "Elimina un pedido y, en cascada, su costo de creación.")
	@ApiResponse(responseCode = "204", description = "Pedido eliminado")
	@ApiResponse(responseCode = "404", description = "Pedido no encontrado")
	public void eliminar(@PathVariable Long id) {
		pedidoService.eliminar(id);
	}
}