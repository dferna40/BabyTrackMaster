package com.babytrackmaster.api_cuidados.service.iml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_cuidados.dto.TipoPanalRequest;
import com.babytrackmaster.api_cuidados.dto.TipoPanalResponse;
import com.babytrackmaster.api_cuidados.entity.TipoPanal;
import com.babytrackmaster.api_cuidados.mapper.TipoPanalMapper;
import com.babytrackmaster.api_cuidados.repository.TipoPanalRepository;
import com.babytrackmaster.api_cuidados.service.TipoPanalService;

@Service
@Transactional
public class TipoPanalServiceImpl implements TipoPanalService {

    private final TipoPanalRepository repo;

    public TipoPanalServiceImpl(TipoPanalRepository repo) {
        this.repo = repo;
    }

    public TipoPanalResponse crear(TipoPanalRequest request) {
        TipoPanal t = TipoPanalMapper.toEntity(request);
        t = repo.save(t);
        return TipoPanalMapper.toResponse(t);
    }

    public TipoPanalResponse actualizar(Long id, TipoPanalRequest request) {
        TipoPanal t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de pañal no encontrado: " + id);
        }
        TipoPanalMapper.copyToEntity(request, t);
        t = repo.save(t);
        return TipoPanalMapper.toResponse(t);
    }

    public void eliminar(Long id) {
        TipoPanal t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de pañal no encontrado: " + id);
        }
        repo.delete(t);
    }

    @Transactional(readOnly = true)
    public TipoPanalResponse obtener(Long id) {
        TipoPanal t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de pañal no encontrado: " + id);
        }
        return TipoPanalMapper.toResponse(t);
    }

    @Transactional(readOnly = true)
    public List<TipoPanalResponse> listar() {
        List<TipoPanal> list = repo.findAll(Sort.by("nombre"));
        List<TipoPanalResponse> resp = new ArrayList<TipoPanalResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(TipoPanalMapper.toResponse(list.get(i)));
        }
        return resp;
    }
}

