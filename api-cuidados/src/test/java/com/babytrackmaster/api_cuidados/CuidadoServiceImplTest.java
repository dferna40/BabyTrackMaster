package com.babytrackmaster.api_cuidados;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_cuidados.dto.QuickStatsResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

@SpringBootTest
@Transactional
class CuidadoServiceImplTest {

    @Autowired
    private CuidadoService service;
    @Autowired
    private TipoCuidadoRepository tipoRepo;
    @Autowired
    private CuidadoRepository cuidadoRepo;

    private Date baseDate;

    @BeforeEach
    void setUp() {
        cuidadoRepo.deleteAll();
        tipoRepo.deleteAll();
        baseDate = date(2024,3,10,0,0);

        TipoCuidado sueno = saveTipo("Sue\u00f1o");
        TipoCuidado panal = saveTipo("Pa\u00f1al");
        TipoCuidado bano = saveTipo("Ba\u00f1o");

        createCuidado(sueno, date(2024,3,10,0,0), date(2024,3,10,4,0), "120");
        createCuidado(sueno, date(2024,3,10,10,0), date(2024,3,10,10,30), "90");
        createCuidado(sueno, date(2024,3,10,16,0), date(2024,3,10,18,0), null);
        createCuidado(panal, date(2024,3,10,3,0), date(2024,3,10,3,5));
        createCuidado(panal, date(2024,3,10,7,0), date(2024,3,10,7,5));
        createCuidado(panal, date(2024,3,10,13,0), date(2024,3,10,13,7));
        createCuidado(bano, date(2024,3,10,18,0), date(2024,3,10,18,20));
    }

    @Test
    void testObtenerEstadisticasRapidas() {
        QuickStatsResponse resp = service.obtenerEstadisticasRapidas(1L,1L, baseDate);
        assertEquals(5.5, resp.getHorasSueno(), 0.001);
        assertEquals(3, resp.getPanales());
        assertEquals(1, resp.getBanos());
    }

    private TipoCuidado saveTipo(String nombre) {
        TipoCuidado t = new TipoCuidado();
        Date now = new Date();
        t.setNombre(nombre);
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return tipoRepo.save(t);
    }

    private Cuidado createCuidado(TipoCuidado tipo, Date inicio, Date fin, String duracion) {
        Cuidado c = new Cuidado();
        c.setBebeId(1L);
        c.setUsuarioId(1L);
        c.setTipo(tipo);
        c.setInicio(inicio);
        c.setFin(fin);
        c.setDuracion(duracion);
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return cuidadoRepo.save(c);
    }

    private Cuidado createCuidado(TipoCuidado tipo, Date inicio, Date fin) {
        return createCuidado(tipo, inicio, fin, null);
    }

    private Date date(int year,int month,int day,int hour,int minute) {
        return Date.from(LocalDateTime.of(year,month,day,hour,minute).atZone(ZoneId.systemDefault()).toInstant());
    }
}
