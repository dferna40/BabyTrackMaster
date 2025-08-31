package com.babytrackmaster.api_cuidados.service.iml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.entity.Cuidado;
import com.babytrackmaster.api_cuidados.mapper.CuidadoMapper;
import com.babytrackmaster.api_cuidados.repository.CuidadoRepository;
import com.babytrackmaster.api_cuidados.repository.TipoCuidadoRepository;
import com.babytrackmaster.api_cuidados.service.CuidadoService;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CuidadoServiceImpl implements CuidadoService {

    private final CuidadoRepository repo;
    private final TipoCuidadoRepository tipoRepo;

    public CuidadoServiceImpl(CuidadoRepository repo, TipoCuidadoRepository tipoRepo) {
        this.repo = repo;
        this.tipoRepo = tipoRepo;
    }

    public CuidadoResponse crear(CuidadoRequest request) {
        Cuidado c = CuidadoMapper.toEntity(request, tipoRepo);
        c = repo.save(c);
        return CuidadoMapper.toResponse(c);
    }

    public CuidadoResponse actualizar(Long id, CuidadoRequest request) {
        Cuidado c = repo.findByIdAndEliminadoFalse(id).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        CuidadoMapper.copyToEntity(request, c, tipoRepo);
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
    public List<CuidadoResponse> listarPorBebe(Long bebeId, Integer limit) {
        List<Cuidado> list;
        if (limit != null) {
            list = repo.findByBebeIdAndEliminadoFalse(bebeId,
                    PageRequest.of(0, limit, Sort.by("inicio").descending()));
        } else {
            list = repo.findByBebeIdAndEliminadoFalseOrderByInicioDesc(bebeId);
        }
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorBebeYTipo(Long bebeId, Long tipoId) {
        List<Cuidado> list = repo.findByBebeIdAndTipo_IdAndEliminadoFalseOrderByInicioDesc(bebeId, tipoId);
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