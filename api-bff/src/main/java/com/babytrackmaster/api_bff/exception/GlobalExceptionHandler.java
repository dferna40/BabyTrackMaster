package com.babytrackmaster.api_bff.exception;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(HttpStatusCodeException.class)
  public ResponseEntity<ApiError> onHttpStatus(HttpStatusCodeException ex, HttpServletRequest req) {
    ApiError err = ApiError.builder()
      .timestamp(OffsetDateTime.now().toString())
      .status(ex.getStatusCode().value())
      .error(ex.getStatusText())
      .message(ex.getResponseBodyAsString())
      .path(req.getRequestURI())
      .build();
    return new ResponseEntity<ApiError>(err, ex.getStatusCode());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> onAny(Exception ex, HttpServletRequest req) {
    ApiError err = ApiError.builder()
      .timestamp(OffsetDateTime.now().toString())
      .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
      .error("Internal Server Error")
      .message(ex.getMessage())
      .path(req.getRequestURI())
      .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
  }
}