package com.babytrackmaster.api_bebe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_bebe.dto.BebeRequest;
import com.babytrackmaster.api_bebe.dto.BebeResponse;
import com.babytrackmaster.api_bebe.entity.Bebe;
import com.babytrackmaster.api_bebe.entity.TipoAlergia;
import com.babytrackmaster.api_bebe.entity.TipoGrupoSanguineo;
import com.babytrackmaster.api_bebe.entity.TipoLactancia;
import com.babytrackmaster.api_bebe.exception.NotFoundException;
import com.babytrackmaster.api_bebe.mapper.BebeMapper;
import com.babytrackmaster.api_bebe.repository.BebeRepository;
import com.babytrackmaster.api_bebe.repository.TipoAlergiaRepository;
import com.babytrackmaster.api_bebe.repository.TipoGrupoSanguineoRepository;
import com.babytrackmaster.api_bebe.repository.TipoLactanciaRepository;
import com.babytrackmaster.api_bebe.service.BebeService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BebeServiceImpl implements BebeService {

    private final BebeRepository repository;
    private final TipoLactanciaRepository tipoLactanciaRepository;
    private final TipoAlergiaRepository tipoAlergiaRepository;
    private final TipoGrupoSanguineoRepository tipoGrupoSanguineoRepository;

    @Override
    public BebeResponse crear(Long usuarioId, BebeRequest request) {
        Bebe entity = BebeMapper.toEntity(request);
        entity.setUsuarioId(usuarioId);
        if (request.getTipoLactanciaId() != null) {
            TipoLactancia tl = tipoLactanciaRepository.findById(request.getTipoLactanciaId())
                    .orElseThrow(() -> new NotFoundException("Tipo de lactancia no encontrado"));
            entity.setTipoLactancia(tl);
        }
        if (request.getTipoAlergiaId() != null) {
            TipoAlergia ta = tipoAlergiaRepository.findById(request.getTipoAlergiaId())
                    .orElseThrow(() -> new NotFoundException("Tipo de alergia no encontrado"));
            entity.setTipoAlergia(ta);
        }
        if (request.getTipoGrupoSanguineoId() != null) {
            TipoGrupoSanguineo tg = tipoGrupoSanguineoRepository.findById(request.getTipoGrupoSanguineoId())
                    .orElseThrow(() -> new NotFoundException("Tipo de grupo sanguíneo no encontrado"));
            entity.setTipoGrupoSanguineo(tg);
        }
        Bebe saved = repository.save(entity);
        return BebeMapper.toResponse(saved);
    }

    @Override
    public BebeResponse actualizar(Long usuarioId, Long id, BebeRequest request) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        if (request.getNombre() != null) {
            bebe.setNombre(request.getNombre());
        }
        if (request.getFechaNacimiento() != null) {
            bebe.setFechaNacimiento(request.getFechaNacimiento());
        }
        if (request.getSexo() != null) {
            bebe.setSexo(request.getSexo());
        }
        if (request.getPesoNacer() != null) {
            bebe.setPesoNacer(request.getPesoNacer());
        }
        if (request.getTallaNacer() != null) {
            bebe.setTallaNacer(request.getTallaNacer());
        }
        if (request.getSemanasGestacion() != null) {
            bebe.setSemanasGestacion(request.getSemanasGestacion());
        }
        if (request.getPerimetroCranealNacer() != null) {
            bebe.setPerimetroCranealNacer(request.getPerimetroCranealNacer());
        }
        if (request.getPesoActual() != null) {
            bebe.setPesoActual(request.getPesoActual());
        }
        if (request.getTallaActual() != null) {
            bebe.setTallaActual(request.getTallaActual());
        }
        if (request.getBebeActivo() != null) {
            bebe.setBebeActivo(request.getBebeActivo());
        }
        if (request.getNumeroSs() != null) {
            bebe.setNumeroSs(request.getNumeroSs());
        }
        if (request.getTipoLactanciaId() != null) {
            TipoLactancia tl = tipoLactanciaRepository.findById(request.getTipoLactanciaId())
                    .orElseThrow(() -> new NotFoundException("Tipo de lactancia no encontrado"));
            bebe.setTipoLactancia(tl);
        }
        if (request.getTipoAlergiaId() != null) {
            TipoAlergia ta = tipoAlergiaRepository.findById(request.getTipoAlergiaId())
                    .orElseThrow(() -> new NotFoundException("Tipo de alergia no encontrado"));
            bebe.setTipoAlergia(ta);
        }
        if (request.getTipoGrupoSanguineoId() != null) {
            TipoGrupoSanguineo tg = tipoGrupoSanguineoRepository.findById(request.getTipoGrupoSanguineoId())
                    .orElseThrow(() -> new NotFoundException("Tipo de grupo sanguíneo no encontrado"));
            bebe.setTipoGrupoSanguineo(tg);
        }
        if (request.getMedicaciones() != null) {
            bebe.setMedicaciones(request.getMedicaciones());
        }
        if (request.getPediatra() != null) {
            bebe.setPediatra(request.getPediatra());
        }
        if (request.getCentroMedico() != null) {
            bebe.setCentroMedico(request.getCentroMedico());
        }
        if (request.getTelefonoCentroMedico() != null) {
            bebe.setTelefonoCentroMedico(request.getTelefonoCentroMedico());
        }
        if (request.getObservaciones() != null) {
            bebe.setObservaciones(request.getObservaciones());
        }
        if (request.getImagenBebe() != null) {
            bebe.setImagenBebe(request.getImagenBebe());
        }
        Bebe saved = repository.save(bebe);
        return BebeMapper.toResponse(saved);
    }

    @Override
    public void eliminar(Long usuarioId, Long id) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        bebe.setEliminado(true);
        repository.save(bebe);
    }

    @Override
    @Transactional(readOnly = true)
    public BebeResponse obtener(Long usuarioId, Long id) {
        Bebe bebe = repository.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId)
            .orElseThrow(() -> new NotFoundException("Bebé no encontrado"));
        return BebeMapper.toResponse(bebe);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BebeResponse> listar(Long usuarioId) {
        return repository.findByUsuarioIdAndEliminadoFalseOrderByFechaNacimientoAsc(usuarioId)
            .stream().map(BebeMapper::toResponse).collect(Collectors.toList());
    }
}
