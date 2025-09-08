package com.babytrackmaster.api_crecimiento;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_crecimiento.entity.Crecimiento;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.repository.CrecimientoRepository;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CrecimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
        tipoPesoId = peso.getId();
        desde = date(2024,3,10,0,0);
        hasta = date(2024,3,11,0,0);
        createCrecimiento(peso, date(2024,3,10,8,0), 3.0);
        createCrecimiento(peso, date(2024,3,10,12,0), 3.5);
        createCrecimiento(peso, date(2024,3,10,18,0), 4.0);
    }

    @Test
    void statsEndpointReturnsAggregatedData() throws Exception {
        mockMvc.perform(get("/api/v1/crecimientos/usuario/1/bebe/1/stats")
                .param("tipoId", String.valueOf(tipoPesoId))
                .param("desde", String.valueOf(desde.getTime()))
                .param("hasta", String.valueOf(hasta.getTime())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.min").value(3.0))
            .andExpect(jsonPath("$.max").value(4.0))
            .andExpect(jsonPath("$.avg").value(3.5));
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
