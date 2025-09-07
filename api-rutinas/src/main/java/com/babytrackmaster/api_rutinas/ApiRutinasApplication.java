package com.babytrackmaster.api_rutinas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class ApiRutinasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRutinasApplication.class, args);
	}

}
