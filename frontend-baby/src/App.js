import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
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
import Ayuda from "./dashboard/pages/Ayuda";
import AnadirBebe from "./dashboard/pages/AnadirBebe";
import ConfiguracionBebe from "./dashboard/pages/ConfiguracionBebe";
import ProtectedRoute from "./components/ProtectedRoute";
import { AuthProvider } from "./context/AuthContext";

function App() {
  return (
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
            <Route path="configuracion-bebe" element={<ConfiguracionBebe />} />
            <Route path="configuracion" element={<Configuracion />} />
            <Route path="acerca" element={<Acerca />} />
            <Route path="ayuda" element={<Ayuda />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
