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

	public PedidoService(PedidoRepository pedidoRepository,
			ClienteRepository clienteRepository,
			CostoCRepository costoCRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.costoCRepository = costoCRepository;
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
		return convertirADto(pedidoRepository.save(pedido));
	}

	public void eliminar(Long id) {
		pedidoRepository.deleteById(id);
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
		pedido.setPrecioInversion(dto.getPrecioInversion());
		pedido.setCantidadFlores(dto.getCantidadFlores());
		pedido.setColorFlores(dto.getColorFlores());
		pedido.setFechaEntrega(dto.getFechaEntrega());
		pedido.setAccesorios(dto.getAccesorios());
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