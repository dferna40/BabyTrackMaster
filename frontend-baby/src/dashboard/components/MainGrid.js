import React, { useContext } from 'react';
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

export default function MainGrid() {
  const { babies } = useContext(BabyContext);

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

        <Grid size={{ xs: 12, md: 6 }}>

        <Grid size={{ xs: 12 }}>

          <QuickActionsCard />
        </Grid>
        <Grid size={{ xs: 12 }}>
          <RecentCareCard />
        </Grid>
        <Grid size={{ xs: 12 }}>
          <UpcomingAppointmentsCard />
        </Grid>
      </Grid>
      <Copyright sx={{ my: 4 }} />
    </Box>
  );
}
