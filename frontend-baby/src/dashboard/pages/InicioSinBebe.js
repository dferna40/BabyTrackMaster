import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import { BabyContext } from '../../context/BabyContext';
import logo from '../../assets/baby-logo.png';

export default function InicioSinBebe() {
  const { babies } = useContext(BabyContext);

  if (babies.length > 0) {
    return <Navigate to="/dashboard" replace />;
  }

  return (
    <Box sx={{ textAlign: 'center', mt: 4 }}>
      <Box
        component="img"
        src={logo}
        alt="Baby logo"
        sx={{ maxWidth: 200, width: '100%', height: 'auto', mb: 2 }}
      />
      <Typography variant="body1">
        La aplicación requiere registrar un bebé para comenzar. Dirígete al menú
        "Añadir bebé".
      </Typography>
    </Box>
  );
}
