import React, { useContext } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import ChevronRightRoundedIcon from '@mui/icons-material/ChevronRightRounded';
import WavingHandRoundedIcon from '@mui/icons-material/WavingHandRounded';
import Box from '@mui/material/Box';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';

export default function HighlightedCard() {
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return 'Buenos días';
    if (hour < 18) return 'Buenas tardes';
    return 'Buenas noches';
  };

  const greeting = `${getGreeting()}, ${user?.nombreCompleto || 'bienvenido'}`;

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
    <Card sx={{ height: '100%', background: 'none', boxShadow: 'none' }}>
      <CardContent sx={{ p: 0 }}>
        <Box
          sx={{
            background: 'linear-gradient(135deg, #6a11cb 0%, #2575fc 100%)',
            borderRadius: 4,
            color: 'common.white',
            p: 2,
            display: 'flex',
            flexDirection: 'column',
            gap: 1,
          }}
        >
          <WavingHandRoundedIcon />
          <Typography component="h2" variant="subtitle2" sx={{ fontWeight: 600 }}>
            {greeting}
          </Typography>
          <Typography sx={{ mb: 1 }}>{summary}</Typography>
          <Button
            variant="contained"
            size="small"
            color="secondary"
            endIcon={<ChevronRightRoundedIcon />}
            onClick={() => navigate('/dashboard/diario')}
            fullWidth={isSmallScreen}
          >
            Ver día
          </Button>
        </Box>
      </CardContent>
    </Card>
  );
}
