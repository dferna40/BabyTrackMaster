package com.babytrackmaster.api_cuidados.service.iml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.mapper.CuidadoMapper;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CuidadoServiceImpl implements CuidadoService {

    private final CuidadoRepository repo;

    public CuidadoServiceImpl(CuidadoRepository repo) {
        this.repo = repo;
    }

    public CuidadoResponse crear(CuidadoRequest request) {
        Cuidado c = CuidadoMapper.toEntity(request);
        c = repo.save(c);
        return CuidadoMapper.toResponse(c);
    }

    public CuidadoResponse actualizar(Long id, CuidadoRequest request) {
        Cuidado c = repo.findByIdAndEliminadoFalse(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        CuidadoMapper.copyToEntity(request, c);
        c = repo.save(c);
        return CuidadoMapper.toResponse(c);
    }

    public void eliminar(Long id) {
        Cuidado c = repo.findByIdAndEliminadoFalse(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        c.setEliminado(true);
        c.setUpdatedAt(new Date());
        repo.save(c);
    }

    @Transactional(readOnly = true)
    public CuidadoResponse obtener(Long id) {
        Cuidado c = repo.findByIdAndEliminadoFalse(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        return CuidadoMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorBebe(Long bebeId) {
        List<Cuidado> list = repo.findByBebeIdAndEliminadoFalseOrderByInicioDesc(bebeId);
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorBebeYTipo(Long bebeId, String tipo) {
        List<Cuidado> list = repo.findByBebeIdAndTipoAndEliminadoFalseOrderByInicioDesc(bebeId, tipo);
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorRango(Long bebeId, Date desde, Date hasta) {
        List<Cuidado> list = repo.findByBebeIdAndInicioBetweenAndEliminadoFalseOrderByInicioDesc(bebeId, desde, hasta);
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }
}