package com.babytrackmaster.api_citas.service;

import com.babytrackmaster.api_citas.dto.CitaCreateDTO;
import com.babytrackmaster.api_citas.dto.CitaResponseDTO;
import com.babytrackmaster.api_citas.entity.Cita;
import com.babytrackmaster.api_citas.entity.EstadoCita;
import com.babytrackmaster.api_citas.entity.TipoCita;
import com.babytrackmaster.api_citas.entity.TipoEspecialidad;
import com.babytrackmaster.api_citas.exception.NotFoundException;
import com.babytrackmaster.api_citas.repository.CitaRepository;
import com.babytrackmaster.api_citas.repository.EstadoCitaRepository;
import com.babytrackmaster.api_citas.repository.TipoCitaRepository;
import com.babytrackmaster.api_citas.repository.TipoEspecialidadRepository;
import com.babytrackmaster.api_citas.service.impl.CitaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceImplTest {

    @Mock
    private CitaRepository repo;
    @Mock
    private TipoCitaRepository tipoRepo;
    @Mock
    private EstadoCitaRepository estadoRepo;
    @Mock
    private TipoEspecialidadRepository especialidadRepo;

    @InjectMocks
    private CitaServiceImpl service;

    private CitaCreateDTO buildDto() {
        CitaCreateDTO dto = new CitaCreateDTO();
        dto.setMotivo("Chequeo");
        dto.setFecha("2025-01-01");
        dto.setHora("10:00");
        dto.setBebeId(1L);
        dto.setTipoId(2L);
        return dto;
    }

    private void mockCommonDependencies() {
        TipoCita tipo = new TipoCita();
        tipo.setId(2L);
        tipo.setNombre("General");
        when(tipoRepo.findById(2L)).thenReturn(Optional.of(tipo));

        EstadoCita estado = new EstadoCita();
        estado.setId(3L);
        estado.setNombre("Programada");
        when(estadoRepo.findByNombreIgnoreCase("Programada")).thenReturn(Optional.of(estado));

        when(repo.save(any(Cita.class))).thenAnswer(inv -> {
            Cita c = inv.getArgument(0);
            c.setId(99L);
            return c;
        });
    }

    @Test
    void crearSinEspecialidad() {
        CitaCreateDTO dto = buildDto();
        mockCommonDependencies();

        CitaResponseDTO res = service.crear(dto, 10L);

        ArgumentCaptor<Cita> captor = ArgumentCaptor.forClass(Cita.class);
        verify(repo).save(captor.capture());
        assertNull(captor.getValue().getTipoEspecialidad());
        assertNull(res.getTipoEspecialidad());
    }

    @Test
    void crearConEspecialidad() {
        CitaCreateDTO dto = buildDto();
        dto.setTipoEspecialidadId(5L);
        mockCommonDependencies();

        TipoEspecialidad esp = new TipoEspecialidad();
        esp.setId(5L);
        esp.setNombre("Pediatría");
        when(especialidadRepo.findById(5L)).thenReturn(Optional.of(esp));

        CitaResponseDTO res = service.crear(dto, 10L);

        ArgumentCaptor<Cita> captor = ArgumentCaptor.forClass(Cita.class);
        verify(repo).save(captor.capture());
        assertNotNull(captor.getValue().getTipoEspecialidad());
        assertNotNull(res.getTipoEspecialidad());
        assertEquals(5L, res.getTipoEspecialidad().getId());
    }

    @Test
    void crearEspecialidadNoEncontradaLanzaExcepcion() {
        CitaCreateDTO dto = buildDto();
        dto.setTipoEspecialidadId(7L);
        mockCommonDependencies();

        when(especialidadRepo.findById(7L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.crear(dto, 10L));
    }
}

