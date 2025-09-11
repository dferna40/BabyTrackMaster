package com.babytrackmaster.api_crecimiento.service.iml;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_crecimiento.dto.CrecimientoRequest;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoResponse;
import com.babytrackmaster.api_crecimiento.dto.CrecimientoStatsResponse;
import com.babytrackmaster.api_crecimiento.entity.Crecimiento;
import com.babytrackmaster.api_crecimiento.mapper.CrecimientoMapper;
import com.babytrackmaster.api_crecimiento.repository.CrecimientoRepository;
import com.babytrackmaster.api_crecimiento.repository.TipoCrecimientoRepository;
import com.babytrackmaster.api_crecimiento.service.CrecimientoService;

@Service
@Transactional
public class CrecimientoServiceImpl implements CrecimientoService {

    private final CrecimientoRepository repo;
    private final TipoCrecimientoRepository tipoRepo;

    public CrecimientoServiceImpl(CrecimientoRepository repo, TipoCrecimientoRepository tipoRepo) {
        this.repo = repo;
        this.tipoRepo = tipoRepo;
    }

    public CrecimientoResponse crear(Long usuarioId, CrecimientoRequest request) {
        Crecimiento c = CrecimientoMapper.toEntity(request, usuarioId, tipoRepo);
        c = repo.save(c);
        return CrecimientoMapper.toResponse(c);
    }

    public CrecimientoResponse actualizar(Long usuarioId, Long id, CrecimientoRequest request) {
        Crecimiento c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Crecimiento no encontrado: " + id);
        }
        CrecimientoMapper.copyToEntity(request, c, usuarioId, tipoRepo);
        c = repo.save(c);
        return CrecimientoMapper.toResponse(c);
    }

    public void eliminar(Long usuarioId, Long id) {
        Crecimiento c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Crecimiento no encontrado: " + id);
        }
        c.setEliminado(true);
        c.setUpdatedAt(new Date());
        repo.save(c);
    }

    @Transactional(readOnly = true)
    public CrecimientoResponse obtener(Long usuarioId, Long id) {
        Crecimiento c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Crecimiento no encontrado: " + id);
        }
        return CrecimientoMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<CrecimientoResponse> listarPorBebe(Long usuarioId, Long bebeId, Integer limit) {
        List<Crecimiento> list;
        if (limit != null) {
            list = repo.findByBebeIdAndUsuarioIdAndEliminadoFalse(bebeId, usuarioId,
                    PageRequest.of(0, limit, Sort.by("fecha").descending()));
        } else {
            list = repo.findByBebeIdAndUsuarioIdAndEliminadoFalseOrderByFechaDesc(bebeId, usuarioId);
        }
        List<CrecimientoResponse> resp = new ArrayList<>();
        for (Crecimiento c : list) {
            resp.add(CrecimientoMapper.toResponse(c));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CrecimientoResponse> listarPorBebeYTipo(Long usuarioId, Long bebeId, Long tipoId, Integer limit) {
        List<Crecimiento> list;
        if (limit != null) {
            list = repo.findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalse(bebeId, tipoId, usuarioId,
                    PageRequest.of(0, limit, Sort.by("fecha").descending()));
        } else {
            list = repo.findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalseOrderByFechaDesc(bebeId, tipoId, usuarioId);
        }
        List<CrecimientoResponse> resp = new ArrayList<>();
        for (Crecimiento c : list) {
            resp.add(CrecimientoMapper.toResponse(c));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CrecimientoResponse> listarPorRango(Long usuarioId, Long bebeId, Date desde, Date hasta) {
        List<Crecimiento> list = repo.findByBebeIdAndUsuarioIdAndFechaBetweenAndEliminadoFalseOrderByFechaDesc(bebeId, usuarioId, desde, hasta);
        List<CrecimientoResponse> resp = new ArrayList<>();
        for (Crecimiento c : list) {
            resp.add(CrecimientoMapper.toResponse(c));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public CrecimientoStatsResponse obtenerEstadisticas(Long usuarioId, Long bebeId, Long tipoId, Date desde, Date hasta) {
        List<Crecimiento> list = repo.findByBebeIdAndUsuarioIdAndTipo_IdAndEliminadoFalseAndFechaBetween(bebeId, usuarioId, tipoId, desde, hasta);
        if (list.isEmpty()) {
            return new CrecimientoStatsResponse();
        }
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0d;
        for (Crecimiento c : list) {
            double v = c.getValor();
            if (v < min) min = v;
            if (v > max) max = v;
            sum += v;
        }
        CrecimientoStatsResponse resp = new CrecimientoStatsResponse();
        resp.setMin(min);
        resp.setMax(max);
        resp.setAvg(sum / list.size());
        return resp;
    }
}
