package com.babytrackmaster.api_cuidados.service.iml;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.babytrackmaster.api_cuidados.dto.CuidadoRequest;
import com.babytrackmaster.api_cuidados.dto.CuidadoResponse;
import com.babytrackmaster.api_cuidados.dto.QuickStatsResponse;
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

    public CuidadoResponse crear(Long usuarioId, CuidadoRequest request) {
        Cuidado c = CuidadoMapper.toEntity(request, usuarioId, tipoRepo);
        c = repo.save(c);
        return CuidadoMapper.toResponse(c);
    }

    public CuidadoResponse actualizar(Long usuarioId, Long id, CuidadoRequest request) {
        Cuidado c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        CuidadoMapper.copyToEntity(request, c, usuarioId, tipoRepo);
        c = repo.save(c);
        return CuidadoMapper.toResponse(c);
    }

    public void eliminar(Long usuarioId, Long id) {
        Cuidado c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        c.setEliminado(true);
        c.setUpdatedAt(new Date());
        repo.save(c);
    }

    @Transactional(readOnly = true)
    public CuidadoResponse obtener(Long usuarioId, Long id) {
        Cuidado c = repo.findByIdAndUsuarioIdAndEliminadoFalse(id, usuarioId).orElse(null);
        if (c == null) {
            throw new IllegalArgumentException("Cuidado no encontrado: " + id);
        }
        return CuidadoMapper.toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorBebe(Long usuarioId, Long bebeId, Integer limit) {
        List<Cuidado> list;
        if (limit != null) {
            list = repo.findByBebeIdAndUsuarioIdAndEliminadoFalse(bebeId, usuarioId,
                    PageRequest.of(0, limit, Sort.by("inicio").descending()));
        } else {
            list = repo.findByBebeIdAndUsuarioIdAndEliminadoFalseOrderByInicioDesc(bebeId, usuarioId);
        }
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorBebeYTipo(Long usuarioId, Long bebeId, Long tipoId) {
        List<Cuidado> list = repo.findByBebeIdAndTipo_IdAndUsuarioIdAndEliminadoFalseOrderByInicioDesc(bebeId, tipoId, usuarioId);
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<CuidadoResponse> listarPorRango(Long usuarioId, Long bebeId, Date desde, Date hasta) {
        List<Cuidado> list = repo.findByBebeIdAndUsuarioIdAndInicioBetweenAndEliminadoFalseOrderByInicioDesc(bebeId, usuarioId, desde, hasta);
        List<CuidadoResponse> resp = new ArrayList<CuidadoResponse>();
        for (int i = 0; i < list.size(); i++) {
            resp.add(CuidadoMapper.toResponse(list.get(i)));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public QuickStatsResponse obtenerEstadisticasRapidas(Long usuarioId, Long bebeId, Date fecha) {
        Date target = (fecha != null) ? fecha : new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(target);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date fin = cal.getTime();

        List<Cuidado> suenos = repo.findByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Sueño", inicio, fin);
        List<Cuidado> numBanos = repo.findByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Baño", inicio, fin);
        List<Cuidado> numPanales = repo.findByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Pañal", inicio, fin);
        //long panales = repo.countByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Pañal", inicio, fin);
        long biberones = repo.countByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Biberon", inicio, fin);
        long pechos = repo.countByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Pecho", inicio, fin);
        //long banos = repo.countByBebeIdAndUsuarioIdAndTipo_NombreAndEliminadoFalseAndInicioBetween(bebeId, usuarioId, "Baño", inicio, fin);

        double minutosSueno = 0d;
        int horasSueno = 0;
        for (Cuidado c : suenos) {
        	horasSueno += (c.getCantidadMl() != null) ? c.getCantidadMl() : 0;
        

        }
        
        int numBanosTotal = 0;
        
        for (Cuidado c : numBanos) {
        	numBanosTotal += (c.getCantidadMl() != null) ? c.getCantidadMl() : 0;
        

        }
        
        int numPanalesTotal = 0;
        
        for (Cuidado c : numPanales) {
        	numPanalesTotal += (c.getCantidadMl() != null) ? c.getCantidadMl() : 0;
        

        }

        QuickStatsResponse resp = new QuickStatsResponse();
        resp.setHorasSueno(horasSueno);
        resp.setPanales(numPanalesTotal);
        resp.setTomas(biberones + pechos);
        resp.setBanos(numBanosTotal);
        return resp;
    }
}