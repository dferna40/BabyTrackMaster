package com.babytrackmaster.api_hitos.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_hitos.dto.HitoRequest;
import com.babytrackmaster.api_hitos.dto.HitoResponse;
import com.babytrackmaster.api_hitos.entity.Hito;
import com.babytrackmaster.api_hitos.exception.NotFoundException;
import com.babytrackmaster.api_hitos.mapper.HitoMapper;
import com.babytrackmaster.api_hitos.repository.HitoRepository;
import com.babytrackmaster.api_hitos.service.HitoService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class HitoServiceImpl implements HitoService {
	
	private static final Logger log = LoggerFactory.getLogger(HitoServiceImpl.class);

    private final HitoRepository repository;

    public HitoResponse crear(Long usuarioId, HitoRequest request) {
    	if (usuarioId == null) {
            throw new IllegalArgumentException("usuarioId no puede ser null");
        }
        // Usa el mapper y SETEA explícitamente el usuarioId
        Hito entity = HitoMapper.toEntity(request);
        entity.setUsuarioId(usuarioId);

        Hito saved = repository.save(entity);
        return HitoMapper.toResponse(saved);
    }

    public HitoResponse actualizar(Long usuarioId, Long id, HitoRequest req) {
        Hito h = repository.findFirstByIdAndUsuarioId(id, usuarioId);
        if (h == null) {
            throw new NotFoundException("Hito no encontrado");
        }

        if (req.getBebeId() != null) {
            h.setBebeId(req.getBebeId());
        }
        if (req.getFecha() != null) {
            h.setFecha(req.getFecha());
        }
        if (req.getDescripcion() != null) {
            h.setDescripcion(req.getDescripcion());
        }
        if (req.getImagenUrl() != null) {
            h.setImagenUrl(req.getImagenUrl());
        }
        if (req.getTitulo() != null) {
            h.setTitulo(req.getTitulo());
        }

        h = repository.save(h);
        return HitoMapper.toResponse(h);
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

//    private Hito getOwned(Long usuarioId, Long id) {
//        Hito h = repository.findFirstByIdAndUsuarioId(id, usuarioId);
//        if (h == null) {
//            log.debug("Hito no encontrado o no pertenece al usuario. id={}, usuarioId={}", id, usuarioId);
//            throw new NotFoundException("Hito no encontrado");
//        }
//        return h;
//    }
    
    private Hito getOwned(Long usuarioId, Long id) {
    	return repository.findByIdAndUsuarioId(id, usuarioId).orElseThrow(() -> {
            log.debug("Hito no encontrado o no pertenece al usuario. id={}, usuarioId={}", id, usuarioId);
            return new NotFoundException("Hito no encontrado");
        });
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