package com.example.sprintboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.ProductoDTO;
import com.example.sprintboot.dto.ViajeDTO;
import com.example.sprintboot.entity.Producto;
import com.example.sprintboot.entity.Viaje;
import com.example.sprintboot.repository.ProductoRepository;
import com.example.sprintboot.repository.ViajeRepository;

@Service
@Transactional
public class ViajeService {

	private final ViajeRepository viajeRepository;
	private final ProductoRepository productoRepository;
	private final InversionTotalService inversionTotalService;

	public ViajeService(ViajeRepository viajeRepository, ProductoRepository productoRepository,
			InversionTotalService inversionTotalService) {
		this.viajeRepository = viajeRepository;
		this.productoRepository = productoRepository;
		this.inversionTotalService = inversionTotalService;
	}

	public List<ViajeDTO> listar() {
		return viajeRepository.findAll().stream()
				.map(this::convertirADto)
				.toList();
	}

	public ViajeDTO obtenerPorId(Long id) {
		Viaje viaje = viajeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + id));
		return convertirADto(viaje);
	}

	public ViajeDTO crear(ViajeDTO dto) {
		Viaje viaje = new Viaje();
		aplicarCampos(viaje, dto);
		Viaje guardado = viajeRepository.save(viaje);
		inversionTotalService.descontar(guardado.getInversion());
		return convertirADto(guardado);
	}

	public ViajeDTO actualizar(Long id, ViajeDTO dto) {
		Viaje viaje = viajeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + id));
		Double inversionAnterior = valorSeguro(viaje.getInversion());
		aplicarCampos(viaje, dto);
		Viaje guardado = viajeRepository.save(viaje);
		inversionTotalService.descontar(valorSeguro(guardado.getInversion()) - inversionAnterior);
		return convertirADto(guardado);
	}

	public void eliminar(Long id) {
		Viaje viaje = viajeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + id));
		inversionTotalService.devolver(valorSeguro(viaje.getInversion()));
		viajeRepository.delete(viaje);
	}

	public List<ProductoDTO> listarProductos(Long viajeId) {
		return productoRepository.findByViajeId(viajeId).stream()
				.map(this::convertirADto)
				.toList();
	}

	public ProductoDTO agregarProducto(Long viajeId, ProductoDTO dto) {
		Viaje viaje = viajeRepository.findById(viajeId)
				.orElseThrow(() -> new RuntimeException("Viaje no encontrado con id " + viajeId));
		Producto producto = new Producto();
		aplicarProducto(producto, dto);
		viaje.agregarProducto(producto);
		return convertirADto(productoRepository.save(producto));
	}

	public ProductoDTO actualizarProducto(Long productoId, ProductoDTO dto) {
		Producto producto = productoRepository.findById(productoId)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + productoId));
		aplicarProducto(producto, dto);
		return convertirADto(productoRepository.save(producto));
	}

	public void eliminarProducto(Long productoId) {
		productoRepository.deleteById(productoId);
	}

	private void aplicarCampos(Viaje viaje, ViajeDTO dto) {
		viaje.setInversion(dto.getInversion());
		viaje.setFechaCreacion(dto.getFechaCreacion());
		if (dto.getProductos() != null) {
			viaje.getProductos().clear();
			for (ProductoDTO pdto : dto.getProductos()) {
				Producto producto = new Producto();
				aplicarProducto(producto, pdto);
				viaje.agregarProducto(producto);
			}
		}
	}

	private void aplicarProducto(Producto producto, ProductoDTO dto) {
		producto.setNombre(dto.getNombre());
		producto.setCantidad(dto.getCantidad());
		producto.setCostoPorUnidad(dto.getCostoPorUnidad());
	}

	private ViajeDTO convertirADto(Viaje viaje) {
		ViajeDTO dto = new ViajeDTO();
		dto.setId(viaje.getId());
		dto.setInversion(viaje.getInversion());
		dto.setFechaCreacion(viaje.getFechaCreacion());
		dto.setProductos(viaje.getProductos().stream()
				.map(this::convertirADto)
				.toList());
		return dto;
	}

	private ProductoDTO convertirADto(Producto producto) {
		ProductoDTO dto = new ProductoDTO();
		dto.setId(producto.getId());
		dto.setViajeId(producto.getViaje() != null ? producto.getViaje().getId() : null);
		dto.setNombre(producto.getNombre());
		dto.setCantidad(producto.getCantidad());
		dto.setCostoPorUnidad(producto.getCostoPorUnidad());
		return dto;
	}

	private double valorSeguro(Double valor) {
		return valor == null ? 0.0 : valor;
	}
}