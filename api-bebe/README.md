# api-bebe

Microservicio para **gestionar la información del bebé** asociado a cada usuario.

## Descripción
CRUD básico de bebés (nombre, fecha de nacimiento, sexo, etc.).

## Tecnologías usadas
- Java 17
- Spring Boot 3.x (Spring Web)
- **Spring Security** + JWT
- Spring Data JPA + **MySQL**
- Lombok
- springdoc-openapi (**Swagger UI**)
- Spring Cloud Config (cliente)

## Inicialización de datos

Al iniciar la aplicación se insertan valores predeterminados en las tablas `tipo_alergia`, `tipo_grupo_sanguineo` y `tipo_lactancia` si se encuentran vacías.

- **tipo_alergia**: Ninguna, Gluten, Lactosa, Frutos secos, Polen, Ácaros, Medicamentos.
- **tipo_grupo_sanguineo**: O+, O-, A+, A-, B+, B-, AB+, AB-.
- **tipo_lactancia**: Lactancia exclusiva, Lactancia predominante, Lactancia mixta,
  Lactancia complementaria, Lactancia directa, Lactancia diferida,
  Lactancia inducida o relactación, Lactancia en tándem.
