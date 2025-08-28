package com.babytrackmaster.api_bff.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.babytrackmaster.api_bff.http.ForwardHeadersInterceptor;

@Configuration
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate rt = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
    interceptors.add(new ForwardHeadersInterceptor());
    rt.setInterceptors(interceptors);
    return rt;
  }
}
