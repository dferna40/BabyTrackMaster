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
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.entity.TipoPanal;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoPanalRepository;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

@SpringBootTest
@Transactional
class CuidadoServiceImplTest {

    @Autowired
    private CuidadoService service;
    @Autowired
    private TipoCuidadoRepository tipoRepo;
    @Autowired
    private TipoPanalRepository tipoPanalRepo;
    @Autowired
    private CuidadoRepository cuidadoRepo;

    private Date baseDate;
    private Cuidado panalGuardado;

    @BeforeEach
    void setUp() {
        cuidadoRepo.deleteAll();
        tipoRepo.deleteAll();
        tipoPanalRepo.deleteAll();
        baseDate = date(2024,3,10,0,0);

        TipoCuidado sueno = saveTipo("Sue\u00f1o");
        TipoCuidado panal = saveTipo("Pa\u00f1al");
        TipoCuidado bano = saveTipo("Ba\u00f1o");
        TipoPanal pipi = saveTipoPanal("PIPI");

        createCuidado(sueno, null, date(2024,3,10,0,0), date(2024,3,10,4,0), 120);
        createCuidado(sueno, null, date(2024,3,10,10,0), date(2024,3,10,10,30), 90);
        createCuidado(sueno, null, date(2024,3,10,16,0), date(2024,3,10,18,0), null);
        panalGuardado = createCuidado(panal, pipi, date(2024,3,10,3,0), date(2024,3,10,3,5), null, 2);
        createCuidado(panal, pipi, date(2024,3,10,7,0), date(2024,3,10,7,5), null, 1);
        createCuidado(panal, pipi, date(2024,3,10,13,0), date(2024,3,10,13,7), null, 1);
        createCuidado(bano, null, date(2024,3,10,18,0), date(2024,3,10,18,20));
    }

    @Test
    void testObtenerEstadisticasRapidas() {
        QuickStatsResponse resp = service.obtenerEstadisticasRapidas(1L,1L, baseDate);
        assertEquals(5.5, resp.getHorasSueno(), 0.001);
        assertEquals(3, resp.getPanales());
        assertEquals(1, resp.getBanos());
    }

    @Test
    void obtenerDevuelveCantidadPanal() {
        CuidadoResponse resp = service.obtener(1L, panalGuardado.getId());
        assertEquals(2, resp.getCantidadPanal());
    }

    private TipoCuidado saveTipo(String nombre) {
        TipoCuidado t = new TipoCuidado();
        Date now = new Date();
        t.setNombre(nombre);
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return tipoRepo.save(t);
    }

    private TipoPanal saveTipoPanal(String nombre) {
        TipoPanal t = new TipoPanal();
        Date now = new Date();
        t.setNombre(nombre);
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return tipoPanalRepo.save(t);
    }

    private Cuidado createCuidado(TipoCuidado tipo, TipoPanal tipoPanal, Date inicio, Date fin, Integer duracion, Integer cantidadPanal) {
        Cuidado c = new Cuidado();
        c.setBebeId(1L);
        c.setUsuarioId(1L);
        c.setTipo(tipo);
        c.setTipoPanal(tipoPanal);
        c.setInicio(inicio);
        c.setFin(fin);
        c.setDuracion(duracion);
        c.setCantidadPanal(cantidadPanal);
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return cuidadoRepo.save(c);
    }

    private Cuidado createCuidado(TipoCuidado tipo, TipoPanal tipoPanal, Date inicio, Date fin, Integer duracion) {
        return createCuidado(tipo, tipoPanal, inicio, fin, duracion, null);
    }

    private Cuidado createCuidado(TipoCuidado tipo, TipoPanal tipoPanal, Date inicio, Date fin) {
        return createCuidado(tipo, tipoPanal, inicio, fin, null, null);
    }

    private Date date(int year,int month,int day,int hour,int minute) {
        return Date.from(LocalDateTime.of(year,month,day,hour,minute).atZone(ZoneId.systemDefault()).toInstant());
    }
}
