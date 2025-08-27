# 👶 BabyTrackMaster

**BabyTrackMaster** es una aplicación modular desarrollada con arquitectura de microservicios orientada al cuidado de bebés. Está diseñada para padres y cuidadores que desean registrar, organizar y visualizar todos los eventos y datos importantes relacionados con su bebé: cuidados, gastos, hitos, citas, rutinas, diario personal y más.

El objetivo principal es ofrecer una experiencia práctica, segura y personalizada que facilite el seguimiento del crecimiento del bebé y mejore la organización familiar.

---

## 🧱 Arquitectura del Proyecto

El sistema está compuesto por varios microservicios independientes, un frontend en React y una infraestructura orquestada con Docker.

```
BabyTrackMaster/
├── config-server/       # Configuración centralizada con Spring Cloud Config	#Puerto: 8888
├── api-usuarios/        # Autenticación, login, roles, JWT						#Puerto: 8080
├── api-cuidados/        # Registro de biberones, pañales, sueño, baño			#Puerto: 8081
├── api-gastos/          # Control de gastos del bebé							#Puerto: 8082
├── api-hitos/           # Registro de hitos importantes						#Puerto: 8083
├── api-citas/           # Citas médicas y recordatorios						#Puerto: 8084
├── api-diario/          # Diario emocional y personal							#Puerto: 8085
├── api-rutinas/         # Planificación de rutinas del bebé					#Puerto: 8086
├── api-bff/         	 # Agrega y transforma datos de microservicios			#Puerto: 8087
├── api-gateway/         # Puerta de entrada y routing de peticiones			#Puerto: 8090
├── frontend/            # Aplicación React con Material UI						
└── documentacion/       # Documentación funcional, técnica y wireframes		
```

---

## 🧰 Tecnologías Utilizadas

### 🔹🛠️ Backend
- Java 8
- Spring Boot 3.4.2
- Spring Security
- Spring Data JPA
- Hibernate
- Lombok
- Spring Cloud Config 2024.0.0
- JWT (JSON Web Tokens)
- Swagger / OpenAPI

### 🔹🖥️ Frontend
- React
- Material UI
- Axios
- React Router DOM
- JWT-decode

### 🔹 Sistema de control de versiones
- Git 

### 🔹 Herramienta de gestión y automatización de proyectos
- Maven

### 🔹🛢 Base de Datos
- MySQL
	- api_usuario_db
	- api_rutinas_db
	- api_hitos_db
	- api_gastos_db
	- api_diario_db
	- api_cuidado_db
	- api_citas_db

### 🔹 Infraestructura y DevOps
- 🐋 Docker & Docker Compose
- 🤵🏻‍♂️ Jenkins (CI/CD)
- 🔥 Prometheus + Grafana (monitorización)
- ELK Stack (Elasticsearch, Logstash, Kibana) para logs

### 🔹 Otros Servicios
- 📨 Mailtrap / SMTP (envío de correos)
- 📦Amazon S3 o Cloudinary (almacenamiento de imágenes)

---

## 📦 Estado del Proyecto

### ✅ Estructura inicial creada de todos los microservicios
🔹Microservicios creados:

	✅ Microservicio `config-server`

	✅ Microservicio `api-usuarios`

	✅ Microservicio `api-gastos`

	✅ Microservicio `api-hitos`

	✅ Microservicio `api-cuidados`

 	✅ Microservicio `api-rutinas`
  
🔹🔜 En Desarrollo:

	🧱 Microservicio `api-diario`

	🧱 Microservicio `api-bff`

	🧱 Microservicio `api-gateway`

	🧱 Frontend
---

## 🔐 Seguridad

Todos los endpoints están protegidos con JWT y Spring Security. Se utilizan roles y filtros personalizados para restringir accesos según el perfil del usuario.

---

## 📝 Licencia

Este proyecto es de uso personal y educativo. Todos los derechos reservados.

©️ Desarrollado por **David Fernández Ramírez**
