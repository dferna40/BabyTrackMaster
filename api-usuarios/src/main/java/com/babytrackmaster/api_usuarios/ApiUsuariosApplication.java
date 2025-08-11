package com.babytrackmaster.api_usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.babytrackmaster.api_usuarios", // raíz
        "config", "security", "controller", "service" // tus paquetes “raíz” actuales
})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
public class ApiUsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiUsuariosApplication.class, args);
    }
}
