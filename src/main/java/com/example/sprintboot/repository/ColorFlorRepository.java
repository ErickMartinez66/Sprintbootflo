package com.example.sprintboot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.ColorFlor;

@Repository
public interface ColorFlorRepository extends JpaRepository<ColorFlor, Long> {

    List<ColorFlor> findAllByOrderByOrdenAscIdAsc();

    List<ColorFlor> findByActivoTrueOrderByOrdenAscIdAsc();

    Optional<ColorFlor> findByValor(String valor);

    boolean existsByValor(String valor);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}
