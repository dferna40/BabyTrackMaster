# api-usuarios

Microservicio de **autenticación y gestión de usuarios**. Emite y valida tokens JWT.

## Descripción
Proporciona endpoints de registro y login, almacena usuarios y roles, y protege el resto de rutas mediante filtros de seguridad.

## Tecnologías usadas
- Java 17
- Spring Boot 3.x
- Spring Web
- **Spring Security**
- **JWT (jjwt / io.jsonwebtoken)**
- Spring Data JPA + **MySQL**
- Lombok
- springdoc-openapi (**Swagger UI**)
- Spring Cloud Config (cliente)
- Docker (opcional)
