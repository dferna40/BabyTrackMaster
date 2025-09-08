package com.babytrackmaster.api_crecimiento.service.iml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.TipoCrecimientoResponse;
import com.babytrackmaster.api_crecimiento.entity.TipoCrecimiento;
import com.babytrackmaster.api_crecimiento.mapper.TipoCrecimientoMapper;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;
import com.babytrackmaster.api_crecimiento.service.TipoCrecimientoService;

@Service
@Transactional
public class TipoCrecimientoServiceImpl implements TipoCrecimientoService {

    private final TipoCrecimientoRepository repo;

    public TipoCrecimientoServiceImpl(TipoCrecimientoRepository repo) {
        this.repo = repo;
    }

    public TipoCrecimientoResponse crear(TipoCrecimientoRequest request) {
        TipoCrecimiento t = TipoCrecimientoMapper.toEntity(request);
        t = repo.save(t);
        return TipoCrecimientoMapper.toResponse(t);
    }

    public TipoCrecimientoResponse actualizar(Long id, TipoCrecimientoRequest request) {
        TipoCrecimiento t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de crecimiento no encontrado: " + id);
        }
        TipoCrecimientoMapper.copyToEntity(request, t);
        t = repo.save(t);
        return TipoCrecimientoMapper.toResponse(t);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TipoCrecimientoResponse obtener(Long id) {
        TipoCrecimiento t = repo.findById(id).orElse(null);
        if (t == null) {
            throw new IllegalArgumentException("Tipo de crecimiento no encontrado: " + id);
        }
        return TipoCrecimientoMapper.toResponse(t);
    }

    @Transactional(readOnly = true)
    public List<TipoCrecimientoResponse> listar() {
        List<TipoCrecimiento> list = repo.findAll();
        List<TipoCrecimientoResponse> resp = new ArrayList<>();
        for (TipoCrecimiento t : list) {
            resp.add(TipoCrecimientoMapper.toResponse(t));
        }
        return resp;
    }
}
