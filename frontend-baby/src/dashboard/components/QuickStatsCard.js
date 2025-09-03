import React, { useContext, useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { obtenerStatsRapidas } from '../../services/cuidadosService';

export default function QuickStatsCard() {
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);
  const [stats, setStats] = useState({
    horasSueno: 0,
    panales: 0,
    tomas: 0,
    banos: 0,
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      obtenerStatsRapidas(user.id, activeBaby.id)
        .then(({ data }) => setStats(data))
        .catch(() =>
          setStats({ horasSueno: 0, panales: 0, tomas: 0, banos: 0 })
        );
    }
  }, [user, activeBaby]);

  const statsArray = [
    { label: 'Horas de sueño', value: `${stats.horasSueno}h` },
    { label: 'Pañales', value: `${stats.panales}` },
    { label: 'Tomas', value: `${stats.tomas}` },
    { label: 'Baños', value: `${stats.banos}` },
  ];

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Estadísticas rápidas del día
        </Typography>
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
      </CardContent>
    </Card>
  );
}
