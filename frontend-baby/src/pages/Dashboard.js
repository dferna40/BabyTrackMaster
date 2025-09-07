import React, { useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

export default function Dashboard() {
  const { logout } = useContext(AuthContext);
  return (
    <div>
      <h1>Panel</h1>
      <button onClick={logout}>Cerrar sesión</button>
    </div>
  );
}
