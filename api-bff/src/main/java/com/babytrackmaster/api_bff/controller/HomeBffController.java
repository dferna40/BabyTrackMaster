package com.babytrackmaster.api_bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.babytrackmaster.api_bff.dto.ResumenHomeDTO;
import com.babytrackmaster.api_bff.service.HomeService;
import com.babytrackmaster.api_bff.util.Dates;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bff/home")
@Tag(name = "Home BFF")
@RequiredArgsConstructor
public class HomeBffController {

  private final HomeService homeService;

  @Operation(summary = "Resumen para dashboard del frontend")
  @GetMapping("/resumen")
  public ResponseEntity<ResumenHomeDTO> resumen(
      HttpServletRequest request,
      @RequestParam Long usuarioId,
      @RequestParam(required = false) String mes // formato yyyy-MM
  ) {
    String mesAnio = mes != null ? mes : Dates.mesActual();
    ResumenHomeDTO dto = homeService.getResumenHome(request, usuarioId, mesAnio);
    return ResponseEntity.ok(dto);
  }
}
