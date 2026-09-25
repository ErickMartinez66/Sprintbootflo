package com.example.sprintboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.CostoC;

@Repository
public interface CostoCRepository extends JpaRepository<CostoC, Long> {

	Optional<CostoC> findByPedidoId(Long pedidoId);
}