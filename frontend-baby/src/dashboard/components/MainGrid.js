import React, { useContext, useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Copyright from '../internals/components/Copyright';
import UpcomingAppointmentsCard from './UpcomingAppointmentsCard';
import RecentCareCard from './RecentCareCard';
import HighlightedCard from './HighlightedCard';
import QuickActionsCard from './QuickActionsCard';
import StatsOverview from './StatsOverview';
import { BabyContext } from '../../context/BabyContext';
import { listarProximas } from '../../services/citasService';

export default function MainGrid() {
  const { babies, activeBaby } = useContext(BabyContext);
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!activeBaby?.id) return;
    listarProximas(activeBaby.id)
      .then((response) => {
        setAppointments(
          response.data.map((c) => ({
            ...c,
            tipoNombre: c.tipo?.nombre ?? c.tipoNombre,
            tipoId: c.tipo?.id ?? c.tipoId,
          }))
        );
        setError(null);
      })
      .catch((err) => {
        console.error('Error fetching citas:', err);
        setError('Error al cargar las citas.');
        setAppointments([]);
      });
  }, [activeBaby]);

  if (babies.length === 0) {
    return <Navigate to="/dashboard/inicio" replace />;
  }

  return (
    <Box
      sx={{
        width: '100%',
        maxWidth: '100%',
        mx: 'auto',
        p: 2,
      }}
    >
      <Grid container spacing={2}>
        <Grid size={{ xs: 12 }}>
          <HighlightedCard />
        </Grid>
        <Grid size={{ xs: 12 }}>
          <StatsOverview />
        </Grid>
        <Grid size={{ xs: 12 }}>
          <QuickActionsCard />
        </Grid>
        <Grid size={{ xs: 12 }}>
          <Grid container spacing={2}>
            <Grid size={{ xs: 12, md: 6 }}>
              <RecentCareCard />
            </Grid>
            <Grid size={{ xs: 12, md: 6 }}>
              <UpcomingAppointmentsCard appointments={appointments} error={error} />
            </Grid>
          </Grid>
        </Grid>
      </Grid>
      <Copyright sx={{ my: 4 }} />
    </Box>
  );
}
