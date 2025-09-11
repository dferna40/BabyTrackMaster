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
import {
  listarUltimosPorTipo,
  listarTipos,
} from '../../services/crecimientoService';

export default function StatsOverview() {
  const theme = useTheme();
  const cardBg = theme.vars
    ? `rgba(${theme.vars.palette.background.paperChannel} / 1)`
    : theme.palette.background.paper;

  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);

  const [stats, setStats] = useState({
    lastBottle: 'Hace 2h',
    sleepHours: 0,
    diapers: { count: 0, diff: 0 },
    weight: { value: '0 kg', diff: undefined, diffValue: 0 },
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      const yesterdayMillis = Date.now() - 24 * 60 * 60 * 1000;
      Promise.all([
        obtenerStatsRapidas(user.id, activeBaby.id),
        obtenerStatsRapidas(user.id, activeBaby.id, yesterdayMillis),
      ])
        .then(([hoy, ayer]) => {
          const hoyData = hoy.data || {};
          const ayerData = ayer.data || {};
          setStats((prev) => ({
            ...prev,
            sleepHours: hoyData.horasSueno || 0,
            diapers: {
              count: hoyData.panales || 0,
              diff: (hoyData.panales || 0) - (ayerData.panales || 0),
            },
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

      listarTipos()
        .then(({ data }) => {
          const peso = data.find((t) =>
            t.nombre?.toLowerCase().includes('peso')
          );
          if (peso) {
            listarUltimosPorTipo(user.id, activeBaby.id, peso.id, 2)
              .then(({ data: registros }) => {
                if (Array.isArray(registros) && registros.length > 0) {
                  const valorActual = registros[0].valor;
                  const valorAnterior = registros[1]?.valor;
                  const difference =
                    valorAnterior !== undefined
                      ? valorActual - valorAnterior
                      : undefined;
                  setStats((prev) => ({
                    ...prev,
                    weight: {
                      value: `${valorActual.toFixed(1)} kg`,
                      diff:
                        valorAnterior !== undefined
                          ? `${difference >= 0 ? '+' : ''}${difference.toFixed(1)} kg`
                          : undefined,
                      diffValue: difference ?? 0,
                    },
                  }));
                }
              })
              .catch(() => {
                /* ignore errors */
              });
          }
        })
        .catch(() => {
          /* ignore errors */
        });
    }
  }, [user, activeBaby]);

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Card sx={{ backgroundColor: cardBg, color: theme.palette.text.primary }}>
          <CardContent>
            <Stack direction="row" spacing={2} alignItems="center">
              <LocalDrinkIcon />
              <Box>
                <Typography variant="subtitle2">Último biberón</Typography>
                <Typography variant="h6">{stats.lastBottle}</Typography>
              </Box>
            </Stack>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <Card sx={{ backgroundColor: cardBg, color: theme.palette.text.primary }}>
          <CardContent>
            <Stack direction="row" spacing={2} alignItems="center">
              <HotelIcon />
              <Box>
                <Typography variant="subtitle2">Horas de sueño</Typography>
                <Typography variant="h6">{stats.sleepHours}h</Typography>
              </Box>
            </Stack>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <Card sx={{ backgroundColor: cardBg, color: theme.palette.text.primary }}>
          <CardContent>
            <Stack direction="row" spacing={2} alignItems="center">
              <BabyChangingStationIcon />
              <Box>
                <Typography variant="subtitle2">Pañales hoy</Typography>
                <Stack direction="row" spacing={1} alignItems="baseline">
                  <Typography variant="h6">{stats.diapers.count}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    (
                    {stats.diapers.diff > 0
                      ? `${stats.diapers.diff} más que ayer`
                      : stats.diapers.diff < 0
                      ? `${Math.abs(stats.diapers.diff)} menos que ayer`
                      : 'Igual que ayer'}
                    )
                  </Typography>
                </Stack>
              </Box>
            </Stack>
          </CardContent>
        </Card>
      </Grid>
      <Grid item xs={12}>
        <Card sx={{ backgroundColor: cardBg, color: theme.palette.text.primary }}>
          <CardContent>
            <Stack direction="row" spacing={2} alignItems="center">
              <TrendingUpIcon color={stats.weight.diffValue >= 0 ? 'success' : 'error'} />
              <Box>
                <Typography variant="subtitle2">Peso actual</Typography>
                <Stack direction="row" spacing={1} alignItems="baseline">
                  <Typography variant="h6">{stats.weight.value}</Typography>
                  {stats.weight.diff !== undefined && (
                    <Typography variant="body2" color="text.secondary">
                      ({stats.weight.diff})
                    </Typography>
                  )}
                </Stack>
              </Box>
            </Stack>
          </CardContent>
        </Card>
      </Grid>
    </Grid>
  );
}

