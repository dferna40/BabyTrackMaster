import * as React from 'react';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Copyright from '../internals/components/Copyright';
import DailyRoutinesCard from './DailyRoutinesCard';
import UpcomingAppointmentsCard from './UpcomingAppointmentsCard';
import RecentCareCard from './RecentCareCard';
import QuickStatsCard from './QuickStatsCard';

export default function MainGrid() {
  return (
    <Box sx={{ width: '100%', maxWidth: { sm: '100%', md: '1700px' } }}>
      <Grid container spacing={2}>
        <Grid size={{ xs: 12, md: 6 }}>
          <DailyRoutinesCard />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <UpcomingAppointmentsCard />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <RecentCareCard />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <QuickStatsCard />
        </Grid>
      </Grid>
      <Copyright sx={{ my: 4 }} />
    </Box>
  );
}
