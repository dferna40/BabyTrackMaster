package com.babytrackmaster.api_bff.http;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class ForwardHeadersInterceptor implements ClientHttpRequestInterceptor {
	  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
	    // Si llega Authorization, la propagamos
	    // (En SecurityConfig ya validamos el JWT)
	    return execution.execute(request, body);
	  }
	}