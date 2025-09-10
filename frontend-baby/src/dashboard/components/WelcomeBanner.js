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

  // Parse birth date manually to avoid timezone shifts when using the
  // Date constructor with a `YYYY-MM-DD` string. Splitting the string and
  // creating the date with numeric arguments ensures the local timezone is
  // respected and allows `getMonth()` and `getDate()` to work as expected.
  const birthDate = activeBaby
    ? (() => {
        const [y, m, d] = activeBaby.fechaNacimiento.split('-').map(Number);
        return new Date(y, m - 1, d);
      })()
    : null;
  let birthdayMessage;

  if (activeBaby && birthDate) {
    const todayDate = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    const nextBirthday = new Date(
      today.getFullYear(),
      birthDate.getMonth(),
      birthDate.getDate(),
    );
    if (nextBirthday < todayDate) {
      nextBirthday.setFullYear(today.getFullYear() + 1);
    }
    const diffDays = Math.round(
      (nextBirthday - todayDate) / (1000 * 60 * 60 * 24),
    );
    if (diffDays === 1) {
      birthdayMessage = `Mañana es el cumpleaños de ${activeBaby.nombre}`;
    } else if (diffDays === 0) {
      const edad = today.getFullYear() - birthDate.getFullYear();
      birthdayMessage = `¡¡¡Felicidades ${activeBaby.nombre} has cumplido ${edad}!!!`;
    }
  }

  const summary = activeBaby
    ? `Hoy es ${formattedDate}. Recuerda las actividades de ${activeBaby.nombre}.`
    : `Hoy es ${formattedDate}.`;

  return (
    <Card sx={{ width: '100%', background: 'none', boxShadow: 'none' }}>
      <CardContent sx={{ p: 0 }}>
        <Box
          sx={(theme) => ({
            backgroundColor: theme.palette.background.paper,
            color: theme.palette.text.primary,
            borderRadius: 2,
            p: 2,
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            width: '100%',
          })}
        >
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
            <WavingHandRoundedIcon color="inherit" />
            <Typography
              component="h2"
              variant="subtitle2"
              sx={{ fontWeight: 600 }}
              color="inherit"
            >
              {greeting}
            </Typography>
            <Typography color="inherit">{summary}</Typography>
            {birthdayMessage && (
              <Typography color="inherit">{birthdayMessage}</Typography>
            )}
          </Box>
          <IconButton color="inherit">
            <FavoriteBorderIcon color="primary" />
          </IconButton>
        </Box>
      </CardContent>
    </Card>
  );
}
