import React, { useContext } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import WavingHandRoundedIcon from '@mui/icons-material/WavingHandRounded';
import Box from '@mui/material/Box';
import IconButton from '@mui/material/IconButton';
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import { BabyContext } from '../../context/BabyContext';

export default function WelcomeBanner() {
  const { activeBaby } = useContext(BabyContext);

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Buenos días';
    if (hour < 18) return 'Buenas tardes';
    return 'Buenas noches';
  };

  const greeting = `¡${getGreeting()}!`;

  const today = new Date();
  const formattedDate = today.toLocaleDateString('es-ES', {
    weekday: 'long',
    day: 'numeric',
    month: 'long',
  });

  const summary = activeBaby
    ? `Hoy es ${formattedDate}. Recuerda las actividades de ${activeBaby.nombre}.`
    : `Hoy es ${formattedDate}.`;

  return (
    <Card sx={{ width: '100%', background: 'none', boxShadow: 'none' }}>
      <CardContent sx={{ p: 0 }}>
        <Box
          sx={(theme) => ({
            backgroundColor: theme.palette.background.paper,
            borderRadius: 2,
            p: 2,
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            width: '100%',
          })}
        >
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
            <WavingHandRoundedIcon />
            <Typography component="h2" variant="subtitle2" sx={{ fontWeight: 600 }}>
              {greeting}
            </Typography>
            <Typography>{summary}</Typography>
          </Box>
          <IconButton>
            <FavoriteBorderIcon />
          </IconButton>
        </Box>
      </CardContent>
    </Card>
  );
}
