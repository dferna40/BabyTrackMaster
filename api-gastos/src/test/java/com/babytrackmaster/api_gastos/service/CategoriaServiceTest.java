package com.babytrackmaster.api_gastos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.service.impl.CategoriaServiceImpl;

class CategoriaServiceTest {

    private CategoriaGastoRepository categoriaRepository;
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        categoriaRepository = Mockito.mock(CategoriaGastoRepository.class);
        categoriaService = new CategoriaServiceImpl(categoriaRepository);
    }

    @Test
    void crearCategoriaPersisteYDevuelveDTO() {
        CategoriaCreateRequest req = new CategoriaCreateRequest();
        req.setNombre("Lactancia");

        CategoriaGasto guardada = new CategoriaGasto();
        guardada.setId(10L);
        guardada.setNombre("Lactancia");
        when(categoriaRepository.save(any(CategoriaGasto.class))).thenReturn(guardada);

        CategoriaResponse resp = categoriaService.crear(req);

        assertNotNull(resp);
        assertEquals(10L, resp.getId());
        assertEquals("Lactancia", resp.getNombre());
        verify(categoriaRepository).save(any(CategoriaGasto.class));
    }

    @Test
    void listarCategoriasOrdenadas() {
        CategoriaGasto c1 = new CategoriaGasto();
        c1.setId(1L);
        c1.setNombre("Bebidas");
        CategoriaGasto c2 = new CategoriaGasto();
        c2.setId(2L);
        c2.setNombre("Alimentos");

        when(categoriaRepository.findAll(any(Sort.class))).thenAnswer(invocation -> {
            List<CategoriaGasto> list = new ArrayList<CategoriaGasto>();
            list.add(c1);
            list.add(c2);
            list.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));
            return list;
        });

        List<CategoriaResponse> resp = categoriaService.listar();

        assertEquals(2, resp.size());
        assertEquals("Alimentos", resp.get(0).getNombre());
        assertEquals("Bebidas", resp.get(1).getNombre());

        ArgumentCaptor<Sort> captor = ArgumentCaptor.forClass(Sort.class);
        verify(categoriaRepository).findAll(captor.capture());
        assertEquals("nombre", captor.getValue().iterator().next().getProperty());
    }
}
