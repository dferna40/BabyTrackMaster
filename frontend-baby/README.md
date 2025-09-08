# frontend

Aplicación **React** (SPA) para la interfaz de usuario.

## Descripción
Pantallas de autenticación, dashboard, cuidados, gastos, hitos, diario, citas y rutinas. Rutas protegidas con JWT y consumo de APIs vía Axios. La página **Inicio sin bebé** está destinada únicamente a usuarios que aún no tienen bebés registrados.

## Tecnologías usadas
- **React**
- **Material UI**
- **React Router DOM**
- **Axios** (interceptor con token JWT)
- **jwt-decode**
- Gestión de estado con Context / useReducer
- Herramientas: Create React App (o Vite), ESLint/Prettier (opcional)

## Estilos de botones
Para mantener la coherencia en los colores de la interfaz, los componentes deben importar los estilos de `src/theme/buttonStyles.js` en lugar de definir el `sx` inline.

```jsx
import { saveButton, cancelButton } from '../theme/buttonStyles';

<Button sx={saveButton}>Guardar</Button>
<Button sx={cancelButton}>Cancelar</Button>
```
