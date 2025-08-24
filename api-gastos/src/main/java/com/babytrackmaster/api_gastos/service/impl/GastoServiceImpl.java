package com.babytrackmaster.api_gastos.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.babytrackmaster.api_gastos.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.babytrackmaster.api_gastos.dto.GastoCreateRequest;
import com.babytrackmaster.api_gastos.dto.GastoResponse;
import com.babytrackmaster.api_gastos.dto.GastoUpdateRequest;
import com.babytrackmaster.api_gastos.dto.ResumenMensualResponse;
import com.babytrackmaster.api_gastos.entity.CategoriaGasto;
import com.babytrackmaster.api_gastos.entity.Gasto;
import com.babytrackmaster.api_gastos.mapper.GastoMapper;
import com.babytrackmaster.api_gastos.repository.CategoriaGastoRepository;
import com.babytrackmaster.api_gastos.repository.GastoRepository;
import com.babytrackmaster.api_gastos.service.GastoService;

@Service
public class GastoServiceImpl implements GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private CategoriaGastoRepository categoriaRepository;

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
        CategoriaGasto categoria = categoriaRepository.findOneById(req.getCategoriaId());
        if (categoria == null) {
            throw new NotFoundException("Categoría no encontrada");
        }
        g.setCategoria(categoria);
        g.setCantidad(req.getCantidad());
        g.setFecha(req.getFecha());
        g.setDescripcion(req.getDescripcion());
        g.setBebeId(req.getBebeId());
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

    public GastoResponse obtener(Long usuarioId, Long id) {
        Gasto g = gastoRepository.findOneByIdAndUsuario(id, usuarioId);
        if (g == null) {
            throw new NotFoundException("Gasto no encontrado");
        }
        return GastoMapper.toResponse(g);
    }

    public Page<GastoResponse> listarPorMes(Long usuarioId, int anio, int mes, Pageable pageable) {
        YearMonth ym = YearMonth.of(anio, mes);
        LocalDate desde = ym.atDay(1);
        LocalDate hasta = ym.atEndOfMonth();
        Page<Gasto> page = gastoRepository.findByUsuarioAndFechaBetween(usuarioId, desde, hasta, pageable);
        List<GastoResponse> dtos = new ArrayList<GastoResponse>();
        int i = 0;
        while (i < page.getContent().size()) {
            dtos.add(GastoMapper.toResponse(page.getContent().get(i)));
            i++;
        }
        return new PageImpl<GastoResponse>(dtos, pageable, page.getTotalElements());
        // sin streams ni lambdas
    }

    public Page<GastoResponse> listarPorCategoria(Long usuarioId, Long categoriaId, Pageable pageable) {
        Page<Gasto> page = gastoRepository.findByUsuarioAndCategoria(usuarioId, categoriaId, pageable);
        List<GastoResponse> dtos = new ArrayList<GastoResponse>();
        int i = 0;
        while (i < page.getContent().size()) {
            dtos.add(GastoMapper.toResponse(page.getContent().get(i)));
            i++;
        }
        return new PageImpl<GastoResponse>(dtos, pageable, page.getTotalElements());
    }

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
            it.setCategoriaId((Long) r[0]);
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
