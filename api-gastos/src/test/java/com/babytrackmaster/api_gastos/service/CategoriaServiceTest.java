package com.babytrackmaster.api_gastos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
import com.babytrackmaster.api_gastos.dto.CategoriaUpdateRequest;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.service.impl.CategoriaServiceImpl;
import com.babytrackmaster.api_gastos.exception.NotFoundException;

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
    void actualizarCategoriaActualizaNombre() {
        CategoriaUpdateRequest req = new CategoriaUpdateRequest();
        req.setNombre("Ropa");

        CategoriaGasto existente = new CategoriaGasto();
        existente.setId(5L);
        existente.setNombre("Pañales");

        when(categoriaRepository.findOneById(5L)).thenReturn(existente);
        when(categoriaRepository.save(any(CategoriaGasto.class))).thenReturn(existente);

        CategoriaResponse resp = categoriaService.actualizar(5L, req);

        assertNotNull(resp);
        assertEquals(5L, resp.getId());
        assertEquals("Ropa", resp.getNombre());
        verify(categoriaRepository).findOneById(5L);
        verify(categoriaRepository).save(existente);
    }

    @Test
    void actualizarCategoriaNoEncontradaLanzaExcepcion() {
        CategoriaUpdateRequest req = new CategoriaUpdateRequest();
        req.setNombre("Ropa");

        when(categoriaRepository.findOneById(99L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> categoriaService.actualizar(99L, req));
    }
}
