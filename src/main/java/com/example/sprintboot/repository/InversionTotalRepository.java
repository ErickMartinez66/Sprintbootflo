package com.example.sprintboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.sprintboot.entity.InversionTotal;

@Repository
public interface InversionTotalRepository extends JpaRepository<InversionTotal, Long> {
}