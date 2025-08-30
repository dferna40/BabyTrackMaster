# frontend

Aplicación **React** (SPA) para la interfaz de usuario.

## Descripción
Pantallas de autenticación, dashboard, cuidados, gastos, hitos, diario, citas, rutinas y perfil. Rutas protegidas con JWT y consumo de APIs vía Axios.

## Tecnologías usadas
- **React**
- **Material UI**
- **React Router DOM**
- **Axios** (interceptor con token JWT)
- **jwt-decode**
- Gestión de estado con Context / useReducer
- Herramientas: Create React App (o Vite), ESLint/Prettier (opcional)

## Configuración

Crea un archivo `.env` en `frontend-baby/` con tu Client ID de Google OAuth:

```
REACT_APP_GOOGLE_CLIENT_ID=<tu client ID de Google OAuth>
```
