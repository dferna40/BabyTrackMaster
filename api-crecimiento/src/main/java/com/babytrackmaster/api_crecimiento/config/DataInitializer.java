package com.babytrackmaster.api_crecimiento.config;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

	private final TipoCrecimientoRepository tipoCrecimientoRepository;

	@Bean
	public CommandLineRunner loadInitialData() {
		return args -> {
			if (tipoCrecimientoRepository.count() == 0) {
				tipoCrecimientoRepository.saveAll(List.of(createTipoCrecimiento("Peso"),
						createTipoCrecimiento("Talla"), createTipoCrecimiento("Perímetro cefálico")));
			}
		};
	}

	private TipoCrecimiento createTipoCrecimiento(String nombre) {
		TipoCrecimiento tc = new TipoCrecimiento();
		Date now = new Date();
		tc.setNombre(nombre);
		return tc;
	}
}
