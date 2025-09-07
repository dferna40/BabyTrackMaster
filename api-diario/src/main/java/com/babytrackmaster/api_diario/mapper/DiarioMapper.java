package com.babytrackmaster.api_diario.mapper;

import java.time.LocalDate;
import java.time.LocalTime;

import com.babytrackmaster.api_diario.dto.DiarioCreateDTO;
import com.babytrackmaster.api_diario.dto.DiarioResponseDTO;
import com.babytrackmaster.api_diario.dto.DiarioUpdateDTO;
import com.babytrackmaster.api_diario.entity.Diario;

public class DiarioMapper {

    public static Diario toEntity(DiarioCreateDTO dto, Long usuarioId) {
        Diario d = new Diario();
        d.setUsuarioId(usuarioId);
        d.setBebeId(dto.getBebeId());
        d.setFecha(LocalDate.parse(dto.getFecha()));
        d.setHora(LocalTime.parse(dto.getHora()));
        d.setTitulo(dto.getTitulo());
        d.setContenido(dto.getContenido());
        d.setEstadoAnimo(dto.getEstadoAnimo());
        d.setEtiquetas(dto.getEtiquetas());
        d.setFotosUrl(dto.getFotosUrl());
        d.setEliminado(false);
        return d;
    }

    public static void merge(Diario d, DiarioUpdateDTO dto) {
        d.setTitulo(dto.getTitulo());
        d.setContenido(dto.getContenido());
        d.setEstadoAnimo(dto.getEstadoAnimo());
        d.setEtiquetas(dto.getEtiquetas());
        d.setFotosUrl(dto.getFotosUrl());
    }

    public static DiarioResponseDTO toDTO(Diario d) {
        DiarioResponseDTO out = new DiarioResponseDTO();
        out.setId(d.getId());
        out.setUsuarioId(d.getUsuarioId());
        out.setBebeId(d.getBebeId());
        out.setFecha(d.getFecha());
        out.setHora(d.getHora());
        out.setTitulo(d.getTitulo());
        out.setContenido(d.getContenido());
        out.setEstadoAnimo(d.getEstadoAnimo());
        out.setEtiquetas(d.getEtiquetas());
        out.setFotosUrl(d.getFotosUrl());
        return out;
    }
}
