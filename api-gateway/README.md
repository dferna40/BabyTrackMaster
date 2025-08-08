# api-gateway

**Punto de entrada** único a todos los microservicios.

## Descripción
Gestiona el enrutamiento y aplica filtros globales de seguridad (validación de JWT). Oculta los puertos internos.

## Tecnologías usadas
- Java 17
- Spring Boot 3.x
- **Spring Cloud Gateway**
- Filtros globales (pre/post) para **JWT**
- Spring Cloud Config (cliente)
- Docker (opcional)
