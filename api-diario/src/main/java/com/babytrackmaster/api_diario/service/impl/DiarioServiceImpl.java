package com.babytrackmaster.api_diario.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.babytrackmaster.api_diario.dto.DiarioCreateDTO;
import com.babytrackmaster.api_diario.dto.DiarioResponseDTO;
import com.babytrackmaster.api_diario.dto.DiarioUpdateDTO;
import com.babytrackmaster.api_diario.dto.PageResponseDTO;
import com.babytrackmaster.api_diario.entity.Diario;
import com.babytrackmaster.api_diario.exception.NotFoundException;
import com.babytrackmaster.api_diario.mapper.DiarioMapper;
import com.babytrackmaster.api_diario.repository.DiarioRepository;
import com.babytrackmaster.api_diario.service.DiarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiarioServiceImpl implements DiarioService {

    private final DiarioRepository repo;

    @Transactional
    public DiarioResponseDTO crear(Long usuarioId, DiarioCreateDTO dto) {
        Diario d = DiarioMapper.toEntity(dto, usuarioId);
        d = repo.save(d);
        return DiarioMapper.toDTO(d);
    }

    @Transactional(readOnly = true)
    public DiarioResponseDTO obtener(Long usuarioId, Long id) {
        Diario d = repo.findOneByIdAndUsuario(id, usuarioId);
        if (d == null || d.isEliminado()) {
            throw new NotFoundException("Entrada de diario no encontrada");
        }
        return DiarioMapper.toDTO(d);
    }

    @Transactional
    public DiarioResponseDTO actualizar(Long usuarioId, Long id, DiarioUpdateDTO dto) {
        Diario d = repo.findOneByIdAndUsuario(id, usuarioId);
        if (d == null || d.isEliminado()) {
            throw new NotFoundException("Entrada de diario no encontrada");
        }
        DiarioMapper.merge(d, dto);
        d = repo.save(d);
        return DiarioMapper.toDTO(d);
    }

    @Transactional
    public void eliminar(Long usuarioId, Long id) {
        Diario d = repo.findOneByIdAndUsuario(id, usuarioId);
        if (d == null || d.isEliminado()) {
            throw new NotFoundException("Entrada de diario no encontrada");
        }
        d.setEliminado(true);
        repo.save(d);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<DiarioResponseDTO> listarRango(Long usuarioId, String desde, String hasta, int page, int size) {
        Page<Diario> p = repo.findByUsuarioAndRangoFechas(usuarioId,
                LocalDate.parse(desde), LocalDate.parse(hasta),
                PageRequest.of(page, size));
        return mapPage(p);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<DiarioResponseDTO> listarDia(Long usuarioId, String fecha, int page, int size) {
        Page<Diario> p = repo.findByUsuarioAndDia(usuarioId, LocalDate.parse(fecha), PageRequest.of(page, size));
        return mapPage(p);
    }

    @Transactional(readOnly = true)
    public PageResponseDTO<DiarioResponseDTO> listarPorTag(Long usuarioId, String tag, int page, int size) {
        Page<Diario> p = repo.findByUsuarioAndTag(usuarioId, tag, PageRequest.of(page, size));
        return mapPage(p);
    }

    private PageResponseDTO<DiarioResponseDTO> mapPage(Page<Diario> page) {
        List<Diario> list = page.getContent();
        List<DiarioResponseDTO> out = new ArrayList<DiarioResponseDTO>();
        for (int i = 0; i < list.size(); i++) {
            out.add(DiarioMapper.toDTO(list.get(i)));
        }
        PageResponseDTO<DiarioResponseDTO> resp = new PageResponseDTO<DiarioResponseDTO>();
        resp.setContent(out);
        resp.setPage(page.getNumber());
        resp.setSize(page.getSize());
        resp.setTotalElements(page.getTotalElements());
        resp.setTotalPages(page.getTotalPages());
        return resp;
    }
}
