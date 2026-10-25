package com.example.sprintboot.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.example.sprintboot.dto.MesDTO;
import com.example.sprintboot.entity.Mes;
import com.example.sprintboot.entity.Pedido;
import com.example.sprintboot.repository.MesRepository;
import com.example.sprintboot.repository.PedidoRepository;

@Service
public class MesService {

	private final MesRepository mesRepository;
	private final PedidoRepository pedidoRepository;

	public MesService(MesRepository mesRepository, PedidoRepository pedidoRepository) {
		this.mesRepository = mesRepository;
		this.pedidoRepository = pedidoRepository;
	}

	public List<MesDTO> listar() {
		return mesRepository.findAllByOrderByAnioDescMesDesc().stream()
				.map(this::convertirADto)
				.toList();
	}

	public MesDTO obtenerPorId(Long mesId) {
		return mesRepository.findById(mesId)
				.map(this::convertirADto)
				.orElseGet(() -> calcular(mesId));
	}

	public MesDTO calcular(Long mesId) {
		int anio = (int) (mesId / 100);
		int mes = (int) (mesId % 100);
		if (mes < 1 || mes > 12 || anio < 1) {
			throw new IllegalArgumentException("ID de mes inválido: " + mesId);
		}

		LocalDate inicio = LocalDate.of(anio, mes, 1);
		LocalDate finExclusivo = inicio.plusMonths(1);
		List<Pedido> pedidos = pedidoRepository.findByFechaEntregaBetween(inicio, finExclusivo);
		List<Pedido> entregados = pedidos.stream()
				.filter(p -> Pedido.ESTADO_ENTREGADO.equals(p.getEstado()))
				.toList();

		double ganancia = entregados.stream()
				.filter(p -> p.getPrecioGanancia() != null)
				.mapToDouble(Pedido::getPrecioGanancia)
				.sum();

		if (pedidos.isEmpty()) {
			mesRepository.findById(mesId).ifPresent(mesRepository::delete);
			return new MesDTO(mesId, mes, anio, 0.0, 0);
		}

		Mes mesEntity = mesRepository.findById(mesId).orElseGet(Mes::new);
		mesEntity.setId(mesId);
		mesEntity.setAnio(anio);
		mesEntity.setMes(mes);
		mesEntity.setGananciaTotal(ganancia);
		mesEntity.setCantidadPedidos(entregados.size());
		return convertirADto(mesRepository.save(mesEntity));
	}

	public MesDTO calcularPorFecha(LocalDate fecha) {
		if (fecha == null) {
			return null;
		}
		return calcular(fecha.getYear() * 100L + fecha.getMonthValue());
	}

	public void sincronizar() {
		Map<Long, Mes> meses = new TreeMap<>();
		for (Pedido p : pedidoRepository.findAll()) {
			if (p.getFechaEntrega() == null) {
				continue;
			}
			long id = p.getFechaEntrega().getYear() * 100L + p.getFechaEntrega().getMonthValue();
			Mes mes = meses.get(id);
			if (mes == null) {
				mes = new Mes();
				mes.setId(id);
				mes.setAnio(p.getFechaEntrega().getYear());
				mes.setMes(p.getFechaEntrega().getMonthValue());
				mes.setGananciaTotal(0.0);
				mes.setCantidadPedidos(0);
				meses.put(id, mes);
			}
			if (Pedido.ESTADO_ENTREGADO.equals(p.getEstado())) {
				mes.setCantidadPedidos(mes.getCantidadPedidos() + 1);
				if (p.getPrecioGanancia() != null) {
					mes.setGananciaTotal(mes.getGananciaTotal() + p.getPrecioGanancia());
				}
			}
		}

		for (Mes mes : meses.values()) {
			mesRepository.save(mes);
		}

		for (Mes existente : mesRepository.findAll()) {
			if (!meses.containsKey(existente.getId())) {
				mesRepository.delete(existente);
			}
		}
	}

	private MesDTO convertirADto(Mes mes) {
		MesDTO dto = new MesDTO();
		dto.setId(mes.getId());
		dto.setMes(mes.getMes());
		dto.setAnio(mes.getAnio());
		dto.setGananciaTotal(mes.getGananciaTotal());
		dto.setCantidadPedidos(mes.getCantidadPedidos());
		return dto;
	}
}