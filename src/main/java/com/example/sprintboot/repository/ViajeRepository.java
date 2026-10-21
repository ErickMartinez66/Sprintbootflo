package com.example.sprintboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.Viaje;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
}