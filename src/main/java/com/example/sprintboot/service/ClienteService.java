package com.example.sprintboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sprintboot.dto.ClienteDTO;
import com.example.sprintboot.entity.Cliente;
import com.example.sprintboot.repository.ClienteRepository;

@Service
public class ClienteService {

	private final ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	public ClienteDTO crear(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		cliente.setNombre(dto.getNombre());
		cliente.setDireccion(dto.getDireccion());
		cliente.setNumero(dto.getNumero());
		return convertirADto(clienteRepository.save(cliente));
	}

	public List<ClienteDTO> listar() {
		return clienteRepository.findAll().stream()
				.map(this::convertirADto)
				.toList();
	}

	public ClienteDTO obtenerPorId(Long id) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + id));
		return convertirADto(cliente);
	}

	public ClienteDTO actualizar(Long id, ClienteDTO dto) {
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Cliente no encontrado con id " + id));
		cliente.setNombre(dto.getNombre());
		cliente.setDireccion(dto.getDireccion());
		cliente.setNumero(dto.getNumero());
		return convertirADto(clienteRepository.save(cliente));
	}

	public void eliminar(Long id) {
		clienteRepository.deleteById(id);
	}

	public List<ClienteDTO> buscarPorNombre(String nombre) {
		return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
				.map(this::convertirADto)
				.toList();
	}

	private ClienteDTO convertirADto(Cliente c) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(c.getId());
		dto.setNombre(c.getNombre());
		dto.setDireccion(c.getDireccion());
		dto.setNumero(c.getNumero());
		return dto;
	}
}