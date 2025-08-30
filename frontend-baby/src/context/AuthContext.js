import React, { createContext, useEffect, useState } from 'react';
import axios from 'axios';

export const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(null);

  useEffect(() => {
    const storedToken = localStorage.getItem('jwt');
    if (storedToken) {
      setToken(storedToken);
      axios.defaults.headers.common['Authorization'] = `Bearer ${storedToken}`;
    }
  }, []);

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
    localStorage.removeItem('jwt');
    delete axios.defaults.headers.common['Authorization'];
  };

  return (
    <AuthContext.Provider value={{ token, login, loginWithGoogle, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
