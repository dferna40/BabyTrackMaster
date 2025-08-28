package com.babytrackmaster.api_bff.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ApiError {
  private String timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
}