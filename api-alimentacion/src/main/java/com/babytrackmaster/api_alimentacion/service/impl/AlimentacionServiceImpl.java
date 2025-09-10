package com.babytrackmaster.api_alimentacion.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionStatsResponse;
import com.babytrackmaster.api_alimentacion.entity.Alimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.mapper.AlimentacionMapper;
import com.babytrackmaster.api_alimentacion.repository.AlimentacionRepository;
import com.babytrackmaster.api_alimentacion.repository.TipoLactanciaRepository;
import com.babytrackmaster.api_alimentacion.service.AlimentacionService;

@Service
@Transactional
public class AlimentacionServiceImpl implements AlimentacionService {

    private final AlimentacionRepository repo;
    private final TipoLactanciaRepository tipoLactanciaRepo;

    public AlimentacionServiceImpl(AlimentacionRepository repo, TipoLactanciaRepository tipoLactanciaRepo) {
        this.repo = repo;
        this.tipoLactanciaRepo = tipoLactanciaRepo;
    }

    public AlimentacionResponse crear(Long usuarioId, Long bebeId, AlimentacionRequest request) {
        Alimentacion a = AlimentacionMapper.toEntity(request, usuarioId, bebeId);
        a = repo.save(a);
        return AlimentacionMapper.toResponse(a);
    }

    public AlimentacionResponse actualizar(Long usuarioId, Long bebeId, Long id, AlimentacionRequest request) {
        Alimentacion a = repo.findByIdAndUsuarioIdAndBebeIdAndEliminadoFalse(id, usuarioId, bebeId)
                .orElseThrow(() -> new IllegalArgumentException("Alimentacion no encontrada: " + id));
        AlimentacionMapper.copyToEntity(request, a);
        a = repo.save(a);
        return AlimentacionMapper.toResponse(a);
    }

    public void eliminar(Long usuarioId, Long bebeId, Long id) {
        Alimentacion a = repo.findByIdAndUsuarioIdAndBebeIdAndEliminadoFalse(id, usuarioId, bebeId)
                .orElseThrow(() -> new IllegalArgumentException("Alimentacion no encontrada: " + id));
        a.setEliminado(true);
        a.setUpdatedAt(new Date());
        repo.save(a);
    }

    @Transactional(readOnly = true)
    public AlimentacionResponse obtener(Long usuarioId, Long bebeId, Long id) {
        Alimentacion a = repo.findByIdAndUsuarioIdAndBebeIdAndEliminadoFalse(id, usuarioId, bebeId)
                .orElseThrow(() -> new IllegalArgumentException("Alimentacion no encontrada: " + id));
        return AlimentacionMapper.toResponse(a);
    }

    @Transactional(readOnly = true)
    public List<AlimentacionResponse> listar(Long usuarioId, Long bebeId) {
        List<Alimentacion> list = repo.findByUsuarioIdAndBebeIdAndEliminadoFalseOrderByFechaHoraDesc(usuarioId, bebeId);
        List<AlimentacionResponse> resp = new ArrayList<>();
        for (Alimentacion a : list) {
            resp.add(AlimentacionMapper.toResponse(a));
        }
        return resp;
    }

    @Transactional(readOnly = true)
    public List<TipoLactancia> listarTiposLactancia() {
        return tipoLactanciaRepo.findAll();
    }

    @Transactional(readOnly = true)
    public AlimentacionStatsResponse stats(Long usuarioId, Long bebeId) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicioDia = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date finDia = cal.getTime();

        cal.setTime(now);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date inicioSemana = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date finSemana = cal.getTime();

        List<Alimentacion> registrosSemana = repo
                .findByUsuarioIdAndBebeIdAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, inicioSemana, finSemana);
        List<Long> weekly = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            weekly.add(0L);
        }
        Calendar calAux = Calendar.getInstance();
        for (Alimentacion a : registrosSemana) {
            calAux.setTime(a.getFechaHora());
            int day = calAux.get(Calendar.DAY_OF_WEEK);
            int index = day == Calendar.SUNDAY ? 6 : day - Calendar.MONDAY;
            weekly.set(index, weekly.get(index) + 1);
        }

        long lactanciaDia = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.LACTANCIA, inicioDia, finDia);
        long biberonDia = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.BIBERON, inicioDia, finDia);
        long solidosDia = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.SOLIDOS, inicioDia, finDia);

        long lactanciaSemana = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.LACTANCIA, inicioSemana, finSemana);
        long biberonSemana = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.BIBERON, inicioSemana, finSemana);
        long solidosSemana = repo.countByUsuarioIdAndBebeIdAndTipoAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, TipoAlimentacion.SOLIDOS, inicioSemana, finSemana);

        AlimentacionStatsResponse r = new AlimentacionStatsResponse();
        r.setLactanciaDia(lactanciaDia);
        r.setBiberonDia(biberonDia);
        r.setSolidosDia(solidosDia);
        r.setLactanciaSemana(lactanciaSemana);
        r.setBiberonSemana(biberonSemana);
        r.setSolidosSemana(solidosSemana);
        r.setWeekly(weekly);
        long total = lactanciaDia + biberonDia;
        if (total > 0) {
            r.setPorcentajeLactancia((double) lactanciaDia * 100d / total);
            r.setPorcentajeBiberon((double) biberonDia * 100d / total);
        }
        return r;
    }
}
