package com.babytrackmaster.api_bff.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ServiceClient {

  private final RestTemplate restTemplate;

  @Value("${bff.endpoints.gastos}")
  private String gastosBase;

  @Value("${bff.endpoints.diario}")
  private String diarioBase;

  @Value("${bff.endpoints.citas}")
  private String citasBase;

  // GET genérico
  public <T> ResponseEntity<T> get(HttpServletRequest req, String url, Class<T> clazz) {
    HttpEntity<Void> entity = new HttpEntity<Void>(headersFrom(req));
    return restTemplate.exchange(url, HttpMethod.GET, entity, clazz);
  }

  // POST genérico
  public <T> ResponseEntity<T> post(HttpServletRequest req, String url, Object body, Class<T> clazz) {
    HttpEntity<Object> entity = new HttpEntity<Object>(body, headersFrom(req));
    return restTemplate.exchange(url, HttpMethod.POST, entity, clazz);
  }

  // ... put, delete similares

  public String gastosUrl(String path) {
    if (path.startsWith("/")) return gastosBase + path;
    return gastosBase + "/" + path;
  }
  public String diarioUrl(String path) {
    if (path.startsWith("/")) return diarioBase + path;
    return diarioBase + "/" + path;
  }
  public String citasUrl(String path) {
    if (path.startsWith("/")) return citasBase + path;
    return citasBase + "/" + path;
  }

  private HttpHeaders headersFrom(HttpServletRequest req) {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_JSON);
    String auth = req.getHeader("Authorization");
    if (auth != null) h.set("Authorization", auth);
    return h;
  }
}
