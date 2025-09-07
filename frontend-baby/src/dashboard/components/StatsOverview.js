import React, { useContext, useEffect, useState } from 'react';
import { useTheme } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Stack from '@mui/material/Stack';
import Box from '@mui/material/Box';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import HotelIcon from '@mui/icons-material/Hotel';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { obtenerStatsRapidas } from '../../services/cuidadosService';
import { listarRecientes as listarAlimentacionRecientes } from '../../services/alimentacionService';

export default function StatsOverview() {
  const theme = useTheme();
  const cardBg =
    theme.palette.mode === 'light'
      ? theme.palette.grey[50]
      : theme.palette.grey[900];

  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);

  const [stats, setStats] = useState({
    lastBottle: 'Hace 2h',
    sleepHours: 0,
    diapers: { count: 0, diff: 0 },
    weight: { value: 0, diff: 0 },
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      obtenerStatsRapidas(user.id, activeBaby.id)
        .then(({ data }) => {
          setStats((prev) => ({
            ...prev,
            sleepHours: data.horasSueno || 0,
            diapers: { ...prev.diapers, count: data.panales || 0 },
          }));
        })
        .catch(() => {
          /* ignore errors */
        });

      listarAlimentacionRecientes(user.id, activeBaby.id, 1)
        .then(({ data }) => {
          if (Array.isArray(data) && data.length > 0) {
            const last = data[0];
            const date = new Date(
              last.fecha || last.date || last.fechaHora || last.createdAt
            );
            const diffMs = Date.now() - date.getTime();
            const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
            setStats((prev) => ({
              ...prev,
              lastBottle: `Hace ${diffHours}h`,
            }));
          }
        })
        .catch(() => {
          /* ignore errors */
        });

      // Sample values for demo purposes
      setStats((prev) => ({
        ...prev,
        diapers: { ...prev.diapers, diff: 1 },
        weight: { value: 6.2, diff: 0.2 },
      }));
    }
  }, [user, activeBaby]);

  return (
    <Card
      sx={{
        width: '100%',
        backgroundColor: cardBg,
        color: theme.palette.text.primary,
      }}
    >
      <CardContent>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6} md={3}>
            <Stack direction="row" spacing={2} alignItems="center" sx={{ width: '100%' }}>
              <LocalDrinkIcon />
              <Box>
                <Typography variant="subtitle2">Último biberón</Typography>
                <Typography variant="h6">{stats.lastBottle}</Typography>
              </Box>
            </Stack>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Stack direction="row" spacing={2} alignItems="center" sx={{ width: '100%' }}>
              <HotelIcon />
              <Box>
                <Typography variant="subtitle2">Horas de sueño</Typography>
                <Typography variant="h6">{stats.sleepHours}h</Typography>
              </Box>
            </Stack>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Stack direction="row" spacing={2} alignItems="center" sx={{ width: '100%' }}>
              <BabyChangingStationIcon />
              <Box>
                <Typography variant="subtitle2">Pañales hoy</Typography>
                <Stack direction="row" spacing={1} alignItems="baseline">
                  <Typography variant="h6">{stats.diapers.count}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    ({stats.diapers.diff >= 0 ? '+' : ''}{stats.diapers.diff})
                  </Typography>
                </Stack>
              </Box>
            </Stack>
          </Grid>
          <Grid item xs={12} sm={6} md={3}>
            <Stack direction="row" spacing={2} alignItems="center" sx={{ width: '100%' }}>
              <TrendingUpIcon color={stats.weight.diff >= 0 ? 'success' : 'error'} />
              <Box>
                <Typography variant="subtitle2">Peso actual</Typography>
                <Stack direction="row" spacing={1} alignItems="baseline">
                  <Typography variant="h6">{stats.weight.value} kg</Typography>
                  <Typography variant="body2" color="text.secondary">
                    ({stats.weight.diff >= 0 ? '+' : ''}{stats.weight.diff} kg)
                  </Typography>
                </Stack>
              </Box>
            </Stack>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}

