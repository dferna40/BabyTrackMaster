import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import SignInSide from "./sign-in-side/SignInSide";
import SignUp from "./sign-up/SignUp";
import DashboardLayout from "./dashboard/Dashboard";
import MainGrid from "./dashboard/components/MainGrid";
import Cuidados from "./dashboard/pages/Cuidados";
import Gastos from "./dashboard/pages/Gastos";
import Diario from "./dashboard/pages/Diario";
import Citas from "./dashboard/pages/Citas";
import Rutinas from "./dashboard/pages/Rutinas";
import Configuracion from "./dashboard/pages/Configuracion";
import Acerca from "./dashboard/pages/Acerca";
import AnadirBebe from "./dashboard/pages/AnadirBebe";
import EditarBebe from "./dashboard/pages/EditarBebe";
import InicioSinBebe from "./dashboard/pages/InicioSinBebe";
import ProtectedRoute from "./components/ProtectedRoute";
import { AuthProvider } from "./context/AuthContext";

dayjs.locale('es');

function App() {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="es">
      <AuthProvider>
        <Router>
          <Routes>
            <Route path="/" element={<SignInSide />} />
            <Route path="/signup" element={<SignUp />} />
            <Route path="/signin" element={<SignInSide />} />
            <Route
              path="/dashboard/*"
              element={
                <ProtectedRoute>
                  <DashboardLayout />
                </ProtectedRoute>
              }
            >
              <Route index element={<MainGrid />} />
              <Route path="cuidados" element={<Cuidados />} />
              <Route path="gastos" element={<Gastos />} />
              <Route path="diario" element={<Diario />} />
              <Route path="citas" element={<Citas />} />
              <Route path="rutinas" element={<Rutinas />} />
              <Route path="anadir-bebe" element={<AnadirBebe />} />
              <Route path="editar-bebe" element={<EditarBebe />} />
              <Route path="configuracion" element={<Configuracion />} />
              <Route path="acerca" element={<Acerca />} />
              <Route path="inicio" element={<InicioSinBebe />} />
            </Route>
          </Routes>
        </Router>
      </AuthProvider>
    </LocalizationProvider>
  );
}

export default App;
