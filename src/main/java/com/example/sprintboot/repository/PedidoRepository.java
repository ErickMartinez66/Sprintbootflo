package com.example.sprintboot.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	List<Pedido> findByClienteId(Long clienteId);

	List<Pedido> findByColorFlores(String colorFlores);

	List<Pedido> findByFechaEntrega(LocalDate fechaEntrega);

	List<Pedido> findByFechaEntregaBetween(LocalDate inicio, LocalDate finExclusivo);
}