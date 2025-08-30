import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

const stats = [
  { label: 'Horas de sueño', value: '8h' },
  { label: 'Pañales', value: '5' },
  { label: 'Tomas', value: '6' },
  { label: 'Baños', value: '1' },
];

export default function QuickStatsCard() {
  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Estadísticas Rápidas
        </Typography>
        <Grid container spacing={2}>
          {stats.map((stat, index) => (
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
