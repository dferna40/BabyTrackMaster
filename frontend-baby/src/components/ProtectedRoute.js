import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

export default function ProtectedRoute({ children }) {
  const { token, loading } = useContext(AuthContext);
  if (loading) {
    return null;
  }
  if (!token) {
    return <Navigate to="/" replace />;
  }
  return children;
}
