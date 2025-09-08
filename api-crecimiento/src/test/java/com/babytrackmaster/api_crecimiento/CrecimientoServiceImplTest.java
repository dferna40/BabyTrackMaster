package com.babytrackmaster.api_crecimiento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_crecimiento.dto.CrecimientoStatsResponse;
import com.babytrackmaster.api_crecimiento.entity.Crecimiento;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.CrecimientoRepository;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;
import com.babytrackmaster.api_crecimiento.service.CrecimientoService;

@SpringBootTest
@Transactional
class CrecimientoServiceImplTest {

    @Autowired
    private CrecimientoService service;
    @Autowired
    private TipoCrecimientoRepository tipoRepo;
    @Autowired
    private CrecimientoRepository crecimientoRepo;

    private Date desde;
    private Date hasta;
    private Long tipoPesoId;

    @BeforeEach
    void setUp() {
        crecimientoRepo.deleteAll();
        tipoRepo.deleteAll();
        TipoCrecimiento peso = saveTipo("Peso");
        TipoCrecimiento talla = saveTipo("Talla");
        tipoPesoId = peso.getId();
        desde = date(2024,3,10,0,0);
        hasta = date(2024,3,11,0,0);
        createCrecimiento(peso, date(2024,3,10,8,0), 3.0);
        createCrecimiento(peso, date(2024,3,10,12,0), 3.5);
        createCrecimiento(peso, date(2024,3,10,18,0), 4.0);
        createCrecimiento(talla, date(2024,3,10,9,0), 50.0);
    }

    @Test
    void testObtenerEstadisticas() {
        CrecimientoStatsResponse resp = service.obtenerEstadisticas(1L,1L,tipoPesoId,desde,hasta);
        assertEquals(3.0, resp.getMin(), 0.001);
        assertEquals(4.0, resp.getMax(), 0.001);
        assertEquals(3.5, resp.getAvg(), 0.001);
    }

    private TipoCrecimiento saveTipo(String nombre) {
        TipoCrecimiento t = new TipoCrecimiento();
        t.setNombre(nombre);
        return tipoRepo.save(t);
    }

    private Crecimiento createCrecimiento(TipoCrecimiento tipo, Date fecha, Double valor) {
        Crecimiento c = new Crecimiento();
        c.setBebeId(1L);
        c.setUsuarioId(1L);
        c.setTipo(tipo);
        c.setFecha(fecha);
        c.setValor(valor);
        c.setUnidad("kg");
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return crecimientoRepo.save(c);
    }

    private Date date(int year,int month,int day,int hour,int minute) {
        return Date.from(LocalDateTime.of(year,month,day,hour,minute).atZone(ZoneId.systemDefault()).toInstant());
    }
}
