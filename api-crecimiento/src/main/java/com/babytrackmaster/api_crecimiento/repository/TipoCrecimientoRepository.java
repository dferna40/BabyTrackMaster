package com.babytrackmaster.api_crecimiento.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;

public interface TipoCrecimientoRepository extends JpaRepository<TipoCrecimiento, Long> {
	 boolean existsByNombre(String nombre);
}
