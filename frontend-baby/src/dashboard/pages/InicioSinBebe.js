import React from 'react';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import logo from '../../assets/baby-logo.png';

export default function InicioSinBebe() {
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
