# 👶 BabyTrackMaster

**BabyTrackMaster** es una aplicación modular desarrollada con arquitectura de microservicios orientada al cuidado de bebés. Está diseñada para padres y cuidadores que desean registrar, organizar y visualizar todos los eventos y datos importantes relacionados con su bebé: cuidados, gastos, hitos, citas, rutinas, diario personal y más.

El objetivo principal es ofrecer una experiencia práctica, segura y personalizada que facilite el seguimiento del crecimiento del bebé y mejore la organización familiar.

---

## 🧱 Arquitectura del Proyecto

El sistema está compuesto por varios microservicios independientes, un frontend en React y una infraestructura orquestada con Docker.

```
BabyTrackMaster/
├── config-server/       # Configuración centralizada con Spring Cloud Config
├── api-usuarios/        # Autenticación, login, roles, JWT
├── api-cuidados/        # Registro de biberones, pañales, sueño, baño
├── api-gastos/          # Control de gastos del bebé
├── api-hitos/           # Registro de hitos importantes
├── api-citas/           # Citas médicas y recordatorios
├── api-diario/          # Diario emocional y personal
├── api-rutinas/         # Planificación de rutinas del bebé
├── api-gateway/         # Puerta de entrada y routing de peticiones
├── frontend/            # Aplicación React con Material UI
└── documentacion/       # Documentación funcional, técnica y wireframes
```

---

## 🧰 Tecnologías Utilizadas

### 🔹 Backend
- Java 17
- Spring Boot 3.x
- Spring Security
- Spring Data JPA
- Spring Cloud Config
- JWT (JSON Web Tokens)
- Swagger / OpenAPI

### 🔹 Frontend
- React
- Material UI
- Axios
- React Router DOM
- JWT-decode

### 🔹 Base de Datos
- MySQL (una por microservicio)

### 🔹 Infraestructura y DevOps
- Docker & Docker Compose
- Jenkins (CI/CD)
- Prometheus + Grafana (monitorización)
- ELK Stack (Elasticsearch, Logstash, Kibana) para logs

### 🔹 Otros Servicios
- Mailtrap / SMTP (envío de correos)
- Amazon S3 o Cloudinary (almacenamiento de imágenes)

---

## 📦 Estado del Proyecto

✅ Estructura inicial creada  
🔜 Desarrollo del `config-server` y microservicio `api-usuarios`  

---

## 🔐 Seguridad

Todos los endpoints están protegidos con JWT y Spring Security. Se utilizan roles y filtros personalizados para restringir accesos según el perfil del usuario.

---

## 📝 Licencia

Este proyecto es de uso personal y educativo. Todos los derechos reservados.
