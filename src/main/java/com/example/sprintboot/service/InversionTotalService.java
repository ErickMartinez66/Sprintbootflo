package com.example.sprintboot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sprintboot.dto.InversionTotalDTO;
import com.example.sprintboot.entity.InversionTotal;
import com.example.sprintboot.repository.InversionTotalRepository;

@Service
@Transactional
public class InversionTotalService {

	private final InversionTotalRepository inversionTotalRepository;

	public InversionTotalService(InversionTotalRepository inversionTotalRepository) {
		this.inversionTotalRepository = inversionTotalRepository;
	}

	public InversionTotalDTO obtener() {
		return convertirADto(obtenerOCrear());
	}

	public InversionTotalDTO actualizar(InversionTotalDTO dto) {
		InversionTotal inversionTotal = obtenerOCrear();
		inversionTotal.setInversion(dto.getInversion());
		inversionTotal.setInversionDolares(dto.getInversionDolares());
		inversionTotal.setInversionFija(dto.getInversionFija());
		return convertirADto(inversionTotalRepository.save(inversionTotal));
	}

	public void descontar(Double monto) {
		ajustarInversionFija(-valorSeguro(monto));
	}

	public void devolver(Double monto) {
		ajustarInversionFija(valorSeguro(monto));
	}

	private void ajustarInversionFija(double delta) {
		InversionTotal inversionTotal = obtenerOCrear();
		inversionTotal.setInversionFija(valorSeguro(inversionTotal.getInversionFija()) + delta);
		inversionTotalRepository.save(inversionTotal);
	}

	private double valorSeguro(Double valor) {
		return valor == null ? 0.0 : valor;
	}

	private InversionTotal obtenerOCrear() {
		return inversionTotalRepository.findById(InversionTotal.ID_UNICO)
				.orElseGet(() -> {
					InversionTotal nuevo = new InversionTotal(InversionTotal.ID_UNICO, 0.0, 0.0, 0.0);
					return inversionTotalRepository.save(nuevo);
				});
	}

	private InversionTotalDTO convertirADto(InversionTotal inversionTotal) {
		InversionTotalDTO dto = new InversionTotalDTO();
		dto.setId(inversionTotal.getId());
		dto.setInversion(inversionTotal.getInversion());
		dto.setInversionDolares(inversionTotal.getInversionDolares());
		dto.setInversionFija(valorSeguro(inversionTotal.getInversionFija()));
		return dto;
	}
}