package com.example.sprintboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sprintboot.dto.CostoCDTO;
import com.example.sprintboot.entity.CostoC;
import com.example.sprintboot.entity.Pedido;
import com.example.sprintboot.repository.CostoCRepository;
import com.example.sprintboot.repository.PedidoRepository;

@Service
public class CostoCService {

	private final CostoCRepository costoCRepository;
	private final PedidoRepository pedidoRepository;

	public CostoCService(CostoCRepository costoCRepository, PedidoRepository pedidoRepository) {
		this.costoCRepository = costoCRepository;
		this.pedidoRepository = pedidoRepository;
	}

	public CostoCDTO crear(CostoCDTO dto) {
		if (dto.getPedidoId() == null) {
			throw new RuntimeException("El pedidoId es obligatorio para crear un CostoC");
		}
		Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
				.orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + dto.getPedidoId()));
		CostoC costoC = new CostoC();
		aplicarDto(costoC, dto);
		costoC.setPedido(pedido);
		return convertirADto(costoCRepository.save(costoC));
	}

	public List<CostoCDTO> listar() {
		return costoCRepository.findAll().stream()
				.map(this::convertirADto)
				.toList();
	}

	public CostoCDTO obtenerPorId(Long id) {
		CostoC costoC = costoCRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("CostoC no encontrado con id " + id));
		return convertirADto(costoC);
	}

	public CostoCDTO obtenerPorPedido(Long pedidoId) {
		CostoC costoC = costoCRepository.findByPedidoId(pedidoId)
				.orElseThrow(() -> new RuntimeException("CostoC no encontrado para el pedido " + pedidoId));
		return convertirADto(costoC);
	}

	public CostoCDTO actualizar(Long id, CostoCDTO dto) {
		CostoC costoC = costoCRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("CostoC no encontrado con id " + id));
		aplicarDto(costoC, dto);
		if (dto.getPedidoId() != null) {
			Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
					.orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + dto.getPedidoId()));
			costoC.setPedido(pedido);
		}
		return convertirADto(costoCRepository.save(costoC));
	}

	public void eliminar(Long id) {
		costoCRepository.deleteById(id);
	}

	private void aplicarDto(CostoC costoC, CostoCDTO dto) {
		costoC.setPalos(dto.getPalos());
		costoC.setPetalos(dto.getPetalos());
		costoC.setHoja(dto.getHoja());
		costoC.setHojam(dto.getHojam());
		costoC.setLazo(dto.getLazo());
		costoC.setMariposa(dto.getMariposa());
		costoC.setCilicona(dto.getCilicona());
		costoC.setOtro(dto.getOtro());
	}

	private CostoCDTO convertirADto(CostoC c) {
		CostoCDTO dto = new CostoCDTO();
		dto.setId(c.getId());
		if (c.getPedido() != null) {
			dto.setPedidoId(c.getPedido().getId());
		}
		dto.setPalos(c.getPalos());
		dto.setPetalos(c.getPetalos());
		dto.setHoja(c.getHoja());
		dto.setHojam(c.getHojam());
		dto.setLazo(c.getLazo());
		dto.setMariposa(c.getMariposa());
		dto.setCilicona(c.getCilicona());
		dto.setOtro(c.getOtro());
		return dto;
	}
}