package com.babytrackmaster.api_gastos.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.babytrackmaster.api_gastos.dto.CategoriaCreateRequest;
import com.babytrackmaster.api_gastos.dto.CategoriaResponse;
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
    void eliminarCategoriaExistenteLlamaDelete() {
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.setId(5L);
        when(categoriaRepository.findOneById(5L)).thenReturn(categoria);

        categoriaService.eliminar(5L);

        verify(categoriaRepository).delete(categoria);
    }

    @Test
    void eliminarCategoriaNoExistenteLanzaNotFound() {
        when(categoriaRepository.findOneById(5L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> categoriaService.eliminar(5L));
    }
}
