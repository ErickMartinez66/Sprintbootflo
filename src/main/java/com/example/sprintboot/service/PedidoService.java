package com.example.sprintboot.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sprintboot.dto.ClienteDTO;
import com.example.sprintboot.dto.CostoCDTO;
import com.example.sprintboot.dto.PedidoDTO;
import com.example.sprintboot.entity.Cliente;
import com.example.sprintboot.entity.CostoC;
import com.example.sprintboot.entity.Pedido;
import com.example.sprintboot.repository.ClienteRepository;
import com.example.sprintboot.repository.CostoCRepository;
import com.example.sprintboot.repository.PedidoRepository;

@Service
public class PedidoService {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final CostoCRepository costoCRepository;
	private final MesService mesService;

	public PedidoService(PedidoRepository pedidoRepository,
			ClienteRepository clienteRepository,
			CostoCRepository costoCRepository,
			MesService mesService) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.costoCRepository = costoCRepository;
		this.mesService = mesService;
	}

	public PedidoDTO crear(PedidoDTO dto) {
		Pedido pedido = new Pedido();
		aplicarCampos(pedido, dto);

		if (dto.getCliente() != null) {
			pedido.setCliente(resolverCliente(dto.getCliente()));
		}

		Pedido guardado = pedidoRepository.save(pedido);

		if (dto.getCostoC() != null) {
			CostoC costoC = new CostoC();
			aplicarCostoC(costoC, dto.getCostoC());
			costoC.setPedido(guardado);
			costoCRepository.save(costoC);
		}
		mesService.calcularPorFecha(guardado.getFechaEntrega());
		return convertirADto(guardado);
	}

	public List<PedidoDTO> listar() {
		return pedidoRepository.findAll().stream()
				.map(this::convertirADto)
				.toList();
	}

	public PedidoDTO obtenerPorId(Long id) {
		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
		return convertirADto(pedido);
	}

	public PedidoDTO asignarCliente(Long pedidoId, ClienteDTO clienteDTO) {
		Pedido pedido = pedidoRepository.findById(pedidoId)
				.orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + pedidoId));
		pedido.setCliente(resolverCliente(clienteDTO));
		return convertirADto(pedidoRepository.save(pedido));
	}

	public PedidoDTO actualizar(Long id, PedidoDTO dto) {
		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Pedido no encontrado con id " + id));
		LocalDate fechaEntregaAnterior = pedido.getFechaEntrega();
		aplicarCampos(pedido, dto);

		if (dto.getCliente() != null) {
			pedido.setCliente(resolverCliente(dto.getCliente()));
		}

		if (dto.getCostoC() != null) {
			CostoC costoC = pedido.getCostoC();
			if (costoC == null) {
				costoC = new CostoC();
				costoC.setPedido(pedido);
				pedido.setCostoC(costoC);
			}
			aplicarCostoC(costoC, dto.getCostoC());
			costoCRepository.save(costoC);
		}
		Pedido guardado = pedidoRepository.save(pedido);
		mesService.calcularPorFecha(fechaEntregaAnterior);
		mesService.calcularPorFecha(guardado.getFechaEntrega());
		return convertirADto(guardado);
	}

	public void eliminar(Long id) {
		Pedido pedido = pedidoRepository.findById(id).orElse(null);
		pedidoRepository.deleteById(id);
		if (pedido != null) {
			mesService.calcularPorFecha(pedido.getFechaEntrega());
		}
	}

	public List<PedidoDTO> listarPorCliente(Long clienteId) {
		return pedidoRepository.findByClienteId(clienteId).stream()
				.map(this::convertirADto)
				.toList();
	}

	public List<PedidoDTO> listarPorColor(String color) {
		return pedidoRepository.findByColorFlores(color).stream()
				.map(this::convertirADto)
				.toList();
	}

	public List<PedidoDTO> listarPorFecha(LocalDate fecha) {
		return pedidoRepository.findByFechaEntrega(fecha).stream()
				.map(this::convertirADto)
				.toList();
	}

	public List<PedidoDTO> listarPorMes(Long mesId) {
		int anio = (int) (mesId / 100);
		int mes = (int) (mesId % 100);
		if (mes < 1 || mes > 12 || anio < 1) {
			throw new IllegalArgumentException("ID de mes inválido: " + mesId);
		}
		LocalDate inicio = LocalDate.of(anio, mes, 1);
		LocalDate finExclusivo = inicio.plusMonths(1);
		return pedidoRepository.findByFechaEntregaBetween(inicio, finExclusivo).stream()
				.map(this::convertirADto)
				.toList();
	}

	private Cliente resolverCliente(ClienteDTO cdto) {
		Cliente cliente;
		if (cdto.getId() != null) {
			cliente = clienteRepository.findById(cdto.getId())
					.orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + cdto.getId()));
		} else {
			cliente = new Cliente();
			cliente.setNombre(cdto.getNombre());
			cliente.setDireccion(cdto.getDireccion());
			cliente.setNumero(cdto.getNumero());
			clienteRepository.save(cliente);
		}
		return cliente;
	}

	private void aplicarCampos(Pedido pedido, PedidoDTO dto) {
		pedido.setPrecioVenta(dto.getPrecioVenta());
		pedido.setCantidadFlores(dto.getCantidadFlores());
		pedido.setColorFlores(dto.getColorFlores());
		pedido.setFechaEntrega(dto.getFechaEntrega());
		pedido.setAccesorios(dto.getAccesorios());
		calcularInversionYGanancia(pedido);
	}

	private void calcularInversionYGanancia(Pedido pedido) {
		if (pedido.getCantidadFlores() == null) {
			pedido.setPrecioInversion(null);
			pedido.setPrecioGanancia(null);
			return;
		}
		Double inversion = pedido.getCantidadFlores() * Pedido.COSTO_POR_FLOR;
		pedido.setPrecioInversion(inversion);
		if (pedido.getPrecioVenta() != null) {
			pedido.setPrecioGanancia(pedido.getPrecioVenta() - inversion);
		} else {
			pedido.setPrecioGanancia(null);
		}
	}

	private void aplicarCostoC(CostoC costoC, CostoCDTO dto) {
		costoC.setPalos(dto.getPalos());
		costoC.setPetalos(dto.getPetalos());
		costoC.setHoja(dto.getHoja());
		costoC.setHojam(dto.getHojam());
		costoC.setLazo(dto.getLazo());
		costoC.setMariposa(dto.getMariposa());
		costoC.setCilicona(dto.getCilicona());
		costoC.setOtro(dto.getOtro());
	}

	private PedidoDTO convertirADto(Pedido p) {
		PedidoDTO dto = new PedidoDTO();
		dto.setId(p.getId());
		dto.setPrecioVenta(p.getPrecioVenta());
		dto.setPrecioInversion(p.getPrecioInversion());
		dto.setPrecioGanancia(p.getPrecioGanancia());
		dto.setCantidadFlores(p.getCantidadFlores());
		dto.setColorFlores(p.getColorFlores());
		dto.setFechaEntrega(p.getFechaEntrega());
		dto.setAccesorios(p.getAccesorios());

		if (p.getCliente() != null) {
			ClienteDTO clienteDTO = new ClienteDTO();
			clienteDTO.setId(p.getCliente().getId());
			clienteDTO.setNombre(p.getCliente().getNombre());
			clienteDTO.setDireccion(p.getCliente().getDireccion());
			clienteDTO.setNumero(p.getCliente().getNumero());
			dto.setCliente(clienteDTO);
		}
		if (p.getCostoC() != null) {
			CostoC costoC = p.getCostoC();
			CostoCDTO costocDTO = new CostoCDTO();
			costocDTO.setId(costoC.getId());
			costocDTO.setPedidoId(p.getId());
			costocDTO.setPalos(costoC.getPalos());
			costocDTO.setPetalos(costoC.getPetalos());
			costocDTO.setHoja(costoC.getHoja());
			costocDTO.setHojam(costoC.getHojam());
			costocDTO.setLazo(costoC.getLazo());
			costocDTO.setMariposa(costoC.getMariposa());
			costocDTO.setCilicona(costoC.getCilicona());
			costocDTO.setOtro(costoC.getOtro());
			dto.setCostoC(costocDTO);
		}
		return dto;
	}
}