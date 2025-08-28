package com.babytrackmaster.api_bff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CitaDTO {
  private Long id;
  private String titulo;
  private String fecha; // yyyy-MM-dd
  private String hora;  // HH:mm
  private String lugar;
}