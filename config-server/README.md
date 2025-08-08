# config-server

Servidor de configuración centralizada para todos los microservicios.

## Descripción
Expone la configuración externa (archivos `application.yml`/`properties`) desde un repositorio de configuración.
Permite cambiar propiedades sin redeploy y mantener una fuente única de configuración.

## Tecnologías usadas
- Java 17
- Spring Boot 3.x
- **Spring Cloud Config Server**
- Gestión de perfiles (`dev`, `docker`, `prod`)
- Docker (opcional)
