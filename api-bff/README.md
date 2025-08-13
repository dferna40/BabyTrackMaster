# BabyTrackMaster - api-bff (Backend For Frontend)

## 📌 Descripción
`api-bff` es el microservicio **Backend For Frontend** de la aplicación **BabyTrackMaster**.  
Su objetivo es orquestar y unificar la información de varios microservicios internos para el **Dashboard General de Bienvenida** en el frontend, reduciendo la latencia y simplificando la lógica del cliente.

Este servicio centraliza datos de:
- `api-cuidados` → Últimos cuidados y estadísticas rápidas.
- `api-citas` → Próximas citas.
- `api-rutinas` → Rutinas del día.
- `profile-service` → Información del perfil activo del bebé.

---

## 🚀 Características principales
- Un único endpoint `/api/v1/dashboard` que devuelve la información necesaria para el Dashboard.
- Comunicación con microservicios internos mediante **OpenFeign**.
- Validación y reenvío de tokens JWT a microservicios internos.
- Orquestación de llamadas en paralelo con **ExecutorService**.
- Tolerancia a fallos con **Resilience4j** (timeouts, circuit breakers, fallbacks).
- Documentación interactiva con **Swagger/OpenAPI**.
- Monitorización con **Spring Boot Actuator** y métricas para **Prometheus**.

---

## 🛠️ Tecnologías usadas
- **Java 17** (implementación compatible con Java 8)
- **Spring Boot** (Web, Security, Validation)
- **Spring Cloud OpenFeign**
- **Spring Cloud Config** (lectura de configuración centralizada)
- **JWT** (seguridad)
- **Swagger** (documentación)
- **Resilience4j** (tolerancia a fallos)
- **Lombok** (reducción de código boilerplate)
- **Actuator** + **Prometheus** (monitorización)

---

## 📂 Estructura de proyecto (resumen)