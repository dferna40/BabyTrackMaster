package com.babytrackmaster.api_cuidados.config;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.entity.TipoPanal;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoPanalRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final TipoCuidadoRepository tipoCuidadoRepository;
    private final TipoPanalRepository tipoPanalRepository;
   

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            if (tipoCuidadoRepository.count() == 0) {
                tipoCuidadoRepository.saveAll(List.of(
                    createTipoCuidado("Pañal"),
                    createTipoCuidado("Sueño"),
                    createTipoCuidado("Baño")
                ));
            }
            if (tipoPanalRepository.count() == 0) {
                tipoPanalRepository.saveAll(List.of(
                    createTipoPanal("PIPI"),
                    createTipoPanal("CACA"),
                    createTipoPanal("MIXTO")
                ));
            }
        };
    }

    private TipoCuidado createTipoCuidado(String nombre) {
        TipoCuidado tc = new TipoCuidado();
        Date now = new Date();
        tc.setNombre(nombre);
        tc.setCreatedAt(now);
        tc.setUpdatedAt(now);
        return tc;
    }

    private TipoPanal createTipoPanal(String nombre) {
        TipoPanal tp = new TipoPanal();
        Date now = new Date();
        tp.setNombre(nombre);
        tp.setCreatedAt(now);
        tp.setUpdatedAt(now);
        return tp;
    }
}
