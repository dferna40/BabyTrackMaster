package com.babytrackmaster.api_gastos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_gastos.dto.GastoCreateRequest;
import com.babytrackmaster.api_gastos.dto.GastoResponse;
import com.babytrackmaster.api_gastos.dto.GastoUpdateRequest;
import com.babytrackmaster.api_gastos.dto.ResumenMensualResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.entity.Gasto;
import com.babytrackmaster.api_gastos.exception.NotFoundException;
import com.babytrackmaster.api_gastos.mapper.GastoMapper;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.repository.GastoRepository;
import com.babytrackmaster.api_gastos.service.GastoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GastoServiceImpl implements GastoService {

    private final GastoRepository gastoRepository;
    private final CategoriaGastoRepository categoriaRepository;

    public GastoResponse crear(Long usuarioId, GastoCreateRequest req) {
        CategoriaGasto categoria = categoriaRepository.findOneById(req.getCategoriaId());
        if (categoria == null) {
            throw new NotFoundException("Categoría no encontrada");
        }

        Gasto g = new Gasto();
        g.setUsuarioId(usuarioId);
        g.setBebeId(req.getBebeId());
        g.setCategoria(categoria);
        g.setCantidad(req.getCantidad());
        g.setFecha(req.getFecha());
        g.setDescripcion(req.getDescripcion());

        g = gastoRepository.save(g);
        return GastoMapper.toResponse(g);
    }

    public GastoResponse actualizar(Long usuarioId, Long id, GastoUpdateRequest req) {
        Gasto g = gastoRepository.findOneByIdAndUsuario(id, usuarioId);
        if (g == null) {
            throw new NotFoundException("Gasto no encontrado");
        }

        if (req.getCategoriaId() != null) {
            CategoriaGasto categoria = categoriaRepository.findOneById(req.getCategoriaId());
            if (categoria == null) {
                throw new NotFoundException("Categoría no encontrada");
            }
            g.setCategoria(categoria);
        }

        if (req.getCantidad() != null) {
            g.setCantidad(req.getCantidad());
        }
        if (req.getFecha() != null) {
            g.setFecha(req.getFecha());
        }
        if (req.getDescripcion() != null) {
            g.setDescripcion(req.getDescripcion());
        }
        if (req.getBebeId() != null) {
            g.setBebeId(req.getBebeId());
        }

        g = gastoRepository.save(g);
        return GastoMapper.toResponse(g);
    }

    public void eliminar(Long usuarioId, Long id) {
        Gasto g = gastoRepository.findOneByIdAndUsuario(id, usuarioId);
        if (g == null) {
            throw new NotFoundException("Gasto no encontrado");
        }
        gastoRepository.delete(g);
    }

    @Transactional(readOnly = true)
    public GastoResponse obtener(Long usuarioId, Long id) {
        Gasto g = gastoRepository.findOneByIdAndUsuario(id, usuarioId);
        if (g == null) {
            throw new NotFoundException("Gasto no encontrado");
        }
        return GastoMapper.toResponse(g);
    }

    @Transactional(readOnly = true)
    public Page<GastoResponse> listarPorMes(Long usuarioId, int anio, int mes, Pageable pageable) {
        YearMonth ym = YearMonth.of(anio, mes);
        LocalDate desde = ym.atDay(1);
        LocalDate hasta = ym.atEndOfMonth();

        Page<Gasto> page = gastoRepository.findByUsuarioIdAndFechaBetweenOrderByFechaDesc(
                usuarioId, desde, hasta, pageable
        );

        List<GastoResponse> dtos = new ArrayList<GastoResponse>();
        List<Gasto> content = page.getContent();
        int i = 0;
        while (i < content.size()) {
            dtos.add(GastoMapper.toResponse(content.get(i)));
            i++;
        }
        return new PageImpl<GastoResponse>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<GastoResponse> listarPorCategoria(Long usuarioId, Long categoriaId, Pageable pageable) {
        Page<Gasto> page = gastoRepository.findByUsuarioIdAndFechaBetweenOrderByFechaDesc(usuarioId, null, null, pageable);

        List<GastoResponse> dtos = new ArrayList<GastoResponse>();
        List<Gasto> content = page.getContent();
        int i = 0;
        while (i < content.size()) {
            dtos.add(GastoMapper.toResponse(content.get(i)));
            i++;
        }
        return new PageImpl<GastoResponse>(dtos, pageable, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public ResumenMensualResponse resumenMensual(Long usuarioId, int anio, int mes) {
        YearMonth ym = YearMonth.of(anio, mes);
        LocalDate desde = ym.atDay(1);
        LocalDate hasta = ym.atEndOfMonth();

        BigDecimal totalMes = gastoRepository.sumTotalMes(usuarioId, desde, hasta);
        List<Object[]> rows = gastoRepository.sumPorCategoriaDelMes(usuarioId, desde, hasta);

        List<ResumenMensualResponse.ItemCategoria> items = new ArrayList<ResumenMensualResponse.ItemCategoria>();
        int i = 0;
        while (i < rows.size()) {
            Object[] r = rows.get(i);
            ResumenMensualResponse.ItemCategoria it = new ResumenMensualResponse.ItemCategoria();
            // r[0] = categoriaId, r[1] = nombre, r[2] = total
            if (r[0] != null) {
                it.setCategoriaId(((Number) r[0]).longValue());
            }
            it.setCategoriaNombre((String) r[1]);
            it.setTotal((BigDecimal) r[2]);
            items.add(it);
            i++;
        }

        ResumenMensualResponse resp = new ResumenMensualResponse();
        resp.setAnio(anio);
        resp.setMes(mes);
        resp.setTotalMes(totalMes);
        resp.setTotalesPorCategoria(items);
        return resp;
    }
}
