import React, { createContext, useEffect, useState } from 'react';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { getCurrentUser } from '../services/usuarioService';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('jwt'));
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        try {
          const response = await getCurrentUser();
          const data = response.data || {};
          const nombreCompleto = `${data.nombre || ''} ${
            data.apellidos?.split(' ')[0] || ''
          }`.trim();

          let email;
          try {
            const decoded = jwtDecode(token);
            email = decoded?.email;
          } catch (e) {
            email = undefined;
          }

          setUser({
            ...data,
            nombreCompleto,
            email: email || data.email || '',
          });
        } catch (error) {
          setUser(null);
        }
      } else {
        delete axios.defaults.headers.common['Authorization'];
        setUser(null);
      }
      setLoading(false);
    };

    fetchUser();
  }, [token]);

  const login = (newToken) => {
    setToken(newToken);
    localStorage.setItem('jwt', newToken);
    axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
  };

  const loginWithGoogle = (newToken) => {
    login(newToken);
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('jwt');
    delete axios.defaults.headers.common['Authorization'];
  };

  return (
    <AuthContext.Provider value={{ token, user, loading, login, loginWithGoogle, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
