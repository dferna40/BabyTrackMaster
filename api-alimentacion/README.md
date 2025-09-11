Microservicio para el **registro de alimentación**: lactancia, biberón y alimentos sólidos.

## Descripción
Permite CRUD de registros de alimentación para cada bebé y usuario, así como estadísticas básicas.

## Campos principales

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `cantidad_alimento_solido` | INT | Cantidad en gramos del alimento sólido ingerido por el bebé. |

## Ejemplo de uso: registro de sólidos

```bash
curl -X POST "http://localhost:8091/api/v1/alimentacion/usuario/{usuarioId}/bebe/{bebeId}" \
  -H "Content-Type: application/json" \
  -d '{
        "tipoAlimentacion": { "id": 3 },
        "tipoAlimentacionSolido": { "id": 1 },
        "cantidadAlimentoSolido": 80,
        "observaciones": "Puré de verduras"
      }'
```

## Tecnologías usadas
- Java 17
- Spring Boot 3.x (Spring Web)
- **Spring Security** + JWT (validación de token)
- Spring Data JPA + **MySQL**
- Lombok
- springdoc-openapi (**Swagger UI**)
- Spring Cloud Config (cliente)
- Docker (opcional)
