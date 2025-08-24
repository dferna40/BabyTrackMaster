package com.babytrackmaster.api_hitos.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_hitos.dto.HitoRequest;
import com.babytrackmaster.api_hitos.dto.HitoResponse;
import com.babytrackmaster.api_hitos.entity.Hito;
import com.babytrackmaster.api_hitos.exception.ResourceNotFoundException;
import com.babytrackmaster.api_hitos.mapper.HitoMapper;
import com.babytrackmaster.api_hitos.repository.HitoRepository;
import com.babytrackmaster.api_hitos.service.HitoService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class HitoServiceImpl implements HitoService {

    private final HitoRepository repository;

    public HitoResponse crear(Long usuarioId, HitoRequest request) {
        Hito entity = HitoMapper.toEntity(request, usuarioId);
        Hito saved = repository.save(entity);
        return HitoMapper.toResponse(saved);
    }

    public HitoResponse actualizar(Long usuarioId, Long id, HitoRequest request) {
        Hito h = getOwned(usuarioId, id);
        HitoMapper.updateEntity(h, request);
        Hito saved = repository.save(h);
        return HitoMapper.toResponse(saved);
    }

    public void eliminar(Long usuarioId, Long id) {
        Hito h = getOwned(usuarioId, id);
        repository.delete(h);
    }

    @Transactional(readOnly = true)
    public HitoResponse obtener(Long usuarioId, Long id) {
        Hito h = getOwned(usuarioId, id);
        return HitoMapper.toResponse(h);
    }

    @Transactional(readOnly = true)
    public List<HitoResponse> listar(Long usuarioId) {
        List<Hito> lista = repository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return mapList(lista);
    }

    @Transactional(readOnly = true)
    public List<HitoResponse> listarPorBebe(Long usuarioId, Long bebeId) {
        List<Hito> lista = repository.findByUsuarioIdAndBebeIdOrderByFechaDesc(usuarioId, bebeId);
        return mapList(lista);
    }

    @Transactional(readOnly = true)
    public List<HitoResponse> listarPorMes(Long usuarioId, YearMonth mes) {
        LocalDate inicio = mes.atDay(1);
        LocalDate fin = mes.atEndOfMonth();
        List<Hito> lista = repository.findByUsuarioIdAndFechaBetweenOrderByFechaDesc(usuarioId, inicio, fin);
        return mapList(lista);
    }

    @Transactional(readOnly = true)
    public List<HitoResponse> buscarPorTitulo(Long usuarioId, String texto) {
        List<Hito> lista = repository.findByUsuarioIdAndTituloContainingIgnoreCaseOrderByFechaDesc(usuarioId, texto);
        return mapList(lista);
    }

    // --- helpers ---

    private Hito getOwned(Long usuarioId, Long id) {
        Hito h = repository.findById(id).orElse(null);
        if (h == null || !h.getUsuarioId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Hito no encontrado para el usuario actual");
        }
        return h;
    }

    private List<HitoResponse> mapList(List<Hito> lista) {
        List<HitoResponse> res = new ArrayList<HitoResponse>();
        if (lista != null) {
            Iterator<Hito> it = lista.iterator();
            while (it.hasNext()) {
                res.add(HitoMapper.toResponse(it.next()));
            }
        }
        return res;
    }
}