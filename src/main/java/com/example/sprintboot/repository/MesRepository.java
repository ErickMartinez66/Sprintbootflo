package com.example.sprintboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.Mes;

@Repository
public interface MesRepository extends JpaRepository<Mes, Long> {

	List<Mes> findAllByOrderByAnioDescMesDesc();
}