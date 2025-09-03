package com.babytrackmaster.api_cuidados;

import static org.hamcrest.Matchers.closeTo;
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

import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class CuidadoControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
        TipoCuidado biberon = saveTipo("Biberon");
        TipoCuidado pecho = saveTipo("Pecho");
        TipoCuidado bano = saveTipo("Ba\u00f1o");

        createCuidado(sueno, date(2024,3,10,0,0), date(2024,3,10,2,0));
        createCuidado(sueno, date(2024,3,10,10,0), date(2024,3,10,11,30));
        createCuidado(panal, date(2024,3,10,3,0), date(2024,3,10,3,5));
        createCuidado(panal, date(2024,3,10,7,0), date(2024,3,10,7,5));
        createCuidado(panal, date(2024,3,10,13,0), date(2024,3,10,13,7));
        createCuidado(biberon, date(2024,3,10,6,0), date(2024,3,10,6,30));
        createCuidado(biberon, date(2024,3,10,9,0), date(2024,3,10,9,20));
        createCuidado(pecho, date(2024,3,10,15,0), date(2024,3,10,15,15));
        createCuidado(bano, date(2024,3,10,18,0), date(2024,3,10,18,20));
    }

    @Test
    void statsEndpointReturnsAggregatedData() throws Exception {
        mockMvc.perform(get("/api/v1/cuidados/usuario/1/bebe/1/stats")
                .param("fecha", String.valueOf(baseDate.getTime())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.horasSueno", closeTo(3.5, 0.01)))
            .andExpect(jsonPath("$.panales").value(3))
            .andExpect(jsonPath("$.tomas").value(3))
            .andExpect(jsonPath("$.banos").value(1));
    }

    private TipoCuidado saveTipo(String nombre) {
        TipoCuidado t = new TipoCuidado();
        Date now = new Date();
        t.setNombre(nombre);
        t.setCreatedAt(now);
        t.setUpdatedAt(now);
        return tipoRepo.save(t);
    }

    private Cuidado createCuidado(TipoCuidado tipo, Date inicio, Date fin) {
        Cuidado c = new Cuidado();
        c.setBebeId(1L);
        c.setUsuarioId(1L);
        c.setTipo(tipo);
        c.setInicio(inicio);
        c.setFin(fin);
        Date now = new Date();
        c.setCreatedAt(now);
        c.setUpdatedAt(now);
        return cuidadoRepo.save(c);
    }

    private Date date(int year,int month,int day,int hour,int minute) {
        return Date.from(LocalDateTime.of(year,month,day,hour,minute).atZone(ZoneId.systemDefault()).toInstant());
    }
}
