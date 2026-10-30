package com.example.sprintboot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sprintboot.dto.MesDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.service.MesService;
import com.example.sprintboot.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/meses")
@Tag(name = "Meses", description = "Resumen mensual de pedidos: ganancia de entregados y cantidad total de pedidos por mes/año")
public class MesController {

	private final MesService mesService;
	private final PedidoService pedidoService;

	public MesController(MesService mesService, PedidoService pedidoService) {
		this.mesService = mesService;
		this.pedidoService = pedidoService;
	}

	@GetMapping
	@Operation(summary = "Listar meses", description = "Devuelve todos los meses con la ganancia de pedidos entregados y la cantidad total de pedidos, ordenados del más reciente al más antiguo.")
	@ApiResponse(responseCode = "200", description = "Meses encontrados",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = MesDTO.class)))
	public List<MesDTO> listar() {
		return mesService.listar();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener mes por id", description = "Devuelve el resumen del mes (id = anio*100 + mes, ej. 202609). Si aún no está registrado, lo calcula y guarda.")
	@ApiResponse(responseCode = "200", description = "Mes encontrado",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = MesDTO.class)))
	@ApiResponse(responseCode = "400", description = "ID de mes inválido")
	public MesDTO obtener(@PathVariable Long id) {
		return mesService.obtenerPorId(id);
	}

	@GetMapping("/{id}/pedidos")
	@Operation(summary = "Pedidos de un mes", description = "Devuelve los pedidos cuya fecha de entrega pertenece al mes indicado (id = anio*100 + mes).")
	@ApiResponse(responseCode = "200", description = "Pedidos encontrados",
			content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class)))
	@ApiResponse(responseCode = "400", description = "ID de mes inválido")
	public List<PedidoDTO> pedidosDelMes(@PathVariable Long id) {
		return pedidoService.listarPorMes(id);
	}

	@PostMapping("/sync")
	@Operation(summary = "Sincronizar tabla de meses", description = "Regenera toda la tabla mes a partir de los pedidos existentes (suma ganancias de entregados y cuenta todos los pedidos por mes/año).")
	@ApiResponse(responseCode = "204", description = "Tabla de meses sincronizada")
	public void sincronizar() {
		mesService.sincronizar();
	}
}