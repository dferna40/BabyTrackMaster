package com.babytrackmaster.api_bff.service;

import com.babytrackmaster.api_bff.dto.ResumenHomeDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface HomeService {
	  ResumenHomeDTO getResumenHome(HttpServletRequest request, Long usuarioId, String mesAnio);
	}
