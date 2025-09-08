import React, { useContext, useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import { PieChart } from '@mui/x-charts/PieChart';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { obtenerStatsRapidas } from '../../services/cuidadosService';

export default function QuickStatsCard() {
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);
  const [stats, setStats] = useState({
    horasSueno: 0,
    panales: 0,
    banos: 0,
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      obtenerStatsRapidas(user.id, activeBaby.id)
        .then(({ data }) => setStats(data))
        .catch(() =>
          setStats({ horasSueno: 0, panales: 0, banos: 0 })
        );
    }
  }, [user, activeBaby]);

  const statsArray = [
    { label: 'Horas de sueño', value: `${stats.horasSueno}h` },
    { label: 'Pañales', value: `${stats.panales}` },
    { label: 'Baños', value: `${stats.banos}` },
  ];

  const chartData = [
    { id: 0, value: stats.horasSueno, label: 'Horas de sueño' },
    { id: 1, value: stats.panales, label: 'Pañales' },
    { id: 2, value: stats.banos, label: 'Baños' },
  ];

  const hasStats = Object.values(stats).some((value) => value > 0);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Estadísticas rápidas del día
        </Typography>
        {hasStats ? (
          <>
            <PieChart series={[{ data: chartData }]} width={200} height={200} />
            <Grid container spacing={2}>
              {statsArray.map((stat, index) => (
                <Grid item xs={6} key={index}>
                  <Typography variant="h5" component="p">
                    {stat.value}
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {stat.label}
                  </Typography>
                </Grid>
              ))}
            </Grid>
          </>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No hay estadísticas que mostrar para el día de hoy.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
}
