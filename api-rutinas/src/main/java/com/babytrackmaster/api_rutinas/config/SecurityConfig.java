package com.babytrackmaster.api_rutinas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.babytrackmaster.api_rutinas.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Endpoints públicos (como en api-cuidados)
    private static final String[] WHITE_LIST = new String[] {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**",
            // si tienes endpoints públicos adicionales, añádelos aquí
            "/",
            "/error",
            "/favicon.ico",
            "/css/**",
            "/js/**",
            "/images/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
            public void customize(CsrfConfigurer<HttpSecurity> csrf) {
                csrf.disable();
            }
        });

        http.cors(Customizer.withDefaults());

        http.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
            public void customize(SessionManagementConfigurer<HttpSecurity> sm) {
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            }
        });

        http.authorizeHttpRequests(
                new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
                    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
                        auth.requestMatchers(WHITE_LIST).permitAll();
                        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                        // Si tu servicio expone un login local (p.ej. /api/v1/auth/**), destápalo:
                        // auth.requestMatchers("/api/v1/auth/**").permitAll();
                        auth.anyRequest().authenticated();
                    }
                }
        );

        // Filtro JWT antes del filtro estándar de credenciales
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Si en algún punto necesitas el AuthenticationManager (por ejemplo para un login local)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}