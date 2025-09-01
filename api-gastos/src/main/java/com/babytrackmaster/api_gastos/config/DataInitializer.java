package com.babytrackmaster.api_gastos.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final CategoriaGastoRepository categoriaGastoRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (categoriaGastoRepository.count() == 0) {
                categoriaGastoRepository.saveAll(List.of(
                    createCategoria("Pañales"),
                    createCategoria("Alimentación"),
                    createCategoria("Ropa"),
                    createCategoria("Higiene"),
                    createCategoria("Salud"),
                    createCategoria("Juguetes"),
                    createCategoria("Transporte"),
                    createCategoria("Guardería"),
                    createCategoria("Otros")
                ));
            }
        };
    }

    private CategoriaGasto createCategoria(String nombre) {
        CategoriaGasto c = new CategoriaGasto();
        c.setNombre(nombre);
        return c;
    }
}
