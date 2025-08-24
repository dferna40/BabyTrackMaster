package com.babytrackmaster.api_gastos.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
            .title("API Gastos - BabyTrackMaster")
            .version("v1")
            .description("Gestión de gastos del bebé (CRUD, filtros y resumen mensual)"));
    }
}
