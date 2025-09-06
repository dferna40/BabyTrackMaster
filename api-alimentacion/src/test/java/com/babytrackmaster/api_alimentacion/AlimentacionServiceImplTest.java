package com.babytrackmaster.api_alimentacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.entity.Alimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.mapper.AlimentacionMapper;
import com.babytrackmaster.api_alimentacion.repository.AlimentacionRepository;
import com.babytrackmaster.api_alimentacion.service.AlimentacionService;

@SpringBootTest
class AlimentacionServiceImplTest {

    @MockBean
    private AlimentacionRepository repo;

    @Autowired
    private AlimentacionService service;

    @Test
    void crearGuardaYDevuelve() {
        AlimentacionRequest req = new AlimentacionRequest();
        req.setTipo(TipoAlimentacion.BIBERON);
        req.setFechaHora(new Date());
        req.setCantidadMl(120);

        Alimentacion saved = AlimentacionMapper.toEntity(req, 1L, 2L);
        saved.setId(5L);

        when(repo.save(any(Alimentacion.class))).thenReturn(saved);

        AlimentacionResponse resp = service.crear(1L, 2L, req);
        assertEquals(5L, resp.getId());
        verify(repo).save(any(Alimentacion.class));
    }
}
