package com.example.sprintboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.Accesorio;

@Repository
public interface AccesorioRepository extends JpaRepository<Accesorio, Long> {

    Optional<Accesorio> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
