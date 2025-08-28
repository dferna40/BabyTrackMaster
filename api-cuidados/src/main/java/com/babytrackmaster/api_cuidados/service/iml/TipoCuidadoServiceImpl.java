package com.babytrackmaster.api_cuidados.service.iml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_cuidados.dto.TipoCuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.TipoCuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.TipoCuidado;
import com.babytrackmaster.api_cuidados.mapper.TipoCuidadoMapper;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;
import com.babytrackmaster.api_cuidados.service.TipoCuidadoService;

@Service
@Transactional
public class TipoCuidadoServiceImpl implements TipoCuidadoService {

    private final TipoCuidadoRepository repo;

    public TipoCuidadoServiceImpl(TipoCuidadoRepository repo) {
        this.repo = repo;
    }

    public TipoCuidadoResponse crear(TipoCuidadoRequest request) {
        TipoCuidado t = TipoCuidadoMapper.toEntity(request);
        t = repo.save(t);
        return TipoCuidadoMapper.toResponse(t);
    }

    public TipoCuidadoResponse actualizar(Long id, TipoCuidadoRequest request) {
        TipoCuidado t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de cuidado no encontrado: " + id);
        }
        TipoCuidadoMapper.copyToEntity(request, t);
        t = repo.save(t);
        return TipoCuidadoMapper.toResponse(t);
    }

    public void eliminar(Long id) {
        TipoCuidado t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de cuidado no encontrado: " + id);
        }
        repo.delete(t);
    }

    @Transactional(readOnly = true)
    public TipoCuidadoResponse obtener(Long id) {
        TipoCuidado t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de cuidado no encontrado: " + id);
        }
        return TipoCuidadoMapper.toResponse(t);
    }

    @Transactional(readOnly = true)
    public List<TipoCuidadoResponse> listar() {
        List<TipoCuidado> list = repo.findAll(Sort.by("nombre"));
        List<TipoCuidadoResponse> resp = new ArrayList<TipoCuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(TipoCuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }
}
