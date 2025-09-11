package com.babytrackmaster.api_alimentacion.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_alimentacion.dto.AlimentacionRequest;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionResponse;
import com.babytrackmaster.api_alimentacion.dto.AlimentacionStatsResponse;
import com.babytrackmaster.api_alimentacion.entity.Alimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacion;
import com.babytrackmaster.api_alimentacion.entity.TipoLactancia;
import com.babytrackmaster.api_alimentacion.entity.TipoLecheBiberon;
import com.babytrackmaster.api_alimentacion.entity.TipoAlimentacionSolido;
import com.babytrackmaster.api_alimentacion.mapper.AlimentacionMapper;
import com.babytrackmaster.api_alimentacion.repository.AlimentacionRepository;
import com.babytrackmaster.api_alimentacion.repository.TipoLactanciaRepository;
import com.babytrackmaster.api_alimentacion.repository.TipoAlimentacionRepository;
import com.babytrackmaster.api_alimentacion.repository.TipoLecheBiberonRepository;
import com.babytrackmaster.api_alimentacion.repository.TipoAlimentacionSolidoRepository;
import com.babytrackmaster.api_alimentacion.service.AlimentacionService;

@Service
@Transactional
public class AlimentacionServiceImpl implements AlimentacionService {

    private final AlimentacionRepository repo;
    private final TipoLactanciaRepository tipoLactanciaRepo;
    private final TipoAlimentacionRepository tipoAlimentacionRepo;
    private final TipoLecheBiberonRepository tipoBiberonRepo;
    private final TipoAlimentacionSolidoRepository tipoAlimentacionSolidoRepo;

    public AlimentacionServiceImpl(AlimentacionRepository repo,
            TipoLactanciaRepository tipoLactanciaRepo,
            TipoAlimentacionRepository tipoAlimentacionRepo,
            TipoLecheBiberonRepository tipoBiberonRepo,
            TipoAlimentacionSolidoRepository tipoAlimentacionSolidoRepo) {
        this.repo = repo;
        this.tipoLactanciaRepo = tipoLactanciaRepo;
        this.tipoAlimentacionRepo = tipoAlimentacionRepo;
        this.tipoBiberonRepo = tipoBiberonRepo;
        this.tipoAlimentacionSolidoRepo = tipoAlimentacionSolidoRepo;
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
    public List<TipoAlimentacion> listarTiposAlimentacion() {
        return tipoAlimentacionRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<TipoLecheBiberon> listarTiposBiberon() {
        return tipoBiberonRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<TipoAlimentacionSolido> listarTiposAlimentacionSolido() {
        return tipoAlimentacionSolidoRepo.findAll();
    }

    @Transactional(readOnly = true)
    public AlimentacionStatsResponse stats(Long usuarioId, Long bebeId, Long tipoAlimentacionId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inicioDia = now.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        DayOfWeek firstDow = WeekFields.ISO.getFirstDayOfWeek();
        LocalDateTime inicioSemana = now.toLocalDate().with(TemporalAdjusters.previousOrSame(firstDow)).atStartOfDay();
        LocalDateTime finSemana = inicioSemana.plusWeeks(1);

        List<Alimentacion> registrosSemana = tipoAlimentacionId == null
                ? repo.findByUsuarioIdAndBebeIdAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, inicioSemana, finSemana)
                : repo.findByUsuarioIdAndBebeIdAndTipoAlimentacionIdAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId, tipoAlimentacionId, inicioSemana, finSemana);
        List<Long> weekly = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            weekly.add(0L);
        }
        for (Alimentacion a : registrosSemana) {
            DayOfWeek day = a.getFechaHora().getDayOfWeek();
            int index = day.getValue() - 1; // ISO: Monday=1
            weekly.set(index, weekly.get(index) + 1);
        }

        TipoAlimentacion lactancia = tipoAlimentacionRepo.findByNombreIgnoreCase("lactancia").orElse(null);
        TipoAlimentacion biberon = tipoAlimentacionRepo.findByNombreIgnoreCase("biberón").orElse(null);
        TipoAlimentacion solidos = tipoAlimentacionRepo.findByNombreIgnoreCase("sólidos").orElse(null);

        long lactanciaDia = lactancia == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        lactancia, inicioDia, finDia);
        long biberonDia = biberon == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        biberon, inicioDia, finDia);
        long solidosDia = solidos == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        solidos, inicioDia, finDia);

        long lactanciaSemana = lactancia == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        lactancia, inicioSemana, finSemana);
        long biberonSemana = biberon == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        biberon, inicioSemana, finSemana);
        long solidosSemana = solidos == null ? 0 :
                repo.countByUsuarioIdAndBebeIdAndTipoAlimentacionAndFechaHoraBetweenAndEliminadoFalse(usuarioId, bebeId,
                        solidos, inicioSemana, finSemana);

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
