package com.babytrackmaster.api_crecimiento.config;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;

@Configuration
public class DataInitializer {

    private final TipoCrecimientoRepository tipoRepo;

    public DataInitializer(TipoCrecimientoRepository tipoRepo) {
        this.tipoRepo = tipoRepo;
    }

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoRepo.count() == 0) {
                tipoRepo.saveAll(List.of(
                        createTipo("Peso"),
                        createTipo("Estatura"),
                        createTipo("Perímetro craneal")
                ));
            }
        };
    }

    private TipoCrecimiento createTipo(String nombre) {
        TipoCrecimiento tc = new TipoCrecimiento();
        Date now = new Date();
        tc.setNombre(nombre);
        tc.setCreatedAt(now);
        tc.setUpdatedAt(now);
        return tc;
    }
}
