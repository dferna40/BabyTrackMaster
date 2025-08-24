package com.babytrackmaster.api_cuidados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.babytrackmaster.api_cuidados.config", "com.babytrackmaster.api_cuidados.security", "com.babytrackmaster.api_cuidados.controller", "com.babytrackmaster.api_cuidados.service" // tus paquetes “raíz” actuales
})
public class ApiCuidadosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCuidadosApplication.class, args);
	}

}
