package com.example.sprintboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.Arreglo;

@Repository
public interface ArregloRepository extends JpaRepository<Arreglo, Long> {

	List<Arreglo> findByActivoTrueOrderByOrdenAscIdAsc();

	List<Arreglo> findAllByOrderByOrdenAscIdAsc();

	@Modifying
	@Query("UPDATE Arreglo a SET a.activo = false WHERE a.id <> :id")
	void desactivarOtros(@Param("id") Long id);

	@Modifying
	@Query("UPDATE Arreglo a SET a.activo = false")
	void desactivarTodos();
}
