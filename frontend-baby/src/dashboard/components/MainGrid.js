import React, { useContext, useEffect, useState } from 'react';
import { Navigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Copyright from '../internals/components/Copyright';
import DailyRoutinesCard from './DailyRoutinesCard';
import UpcomingAppointmentsCard from './UpcomingAppointmentsCard';
import RecentCareCard from './RecentCareCard';
import QuickStatsCard from './QuickStatsCard';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';
import { getBebesByUsuario } from '../../services/bebesService';

export default function MainGrid() {
  const { babies } = useContext(BabyContext);
  const { user } = useContext(AuthContext);
  const [hasBabies, setHasBabies] = useState(null);

  useEffect(() => {
    const checkBabies = async () => {
      if (babies.length > 0) {
        setHasBabies(true);
        return;
      }
      if (!user?.id) {
        setHasBabies(false);
        return;
      }
      try {
        const response = await getBebesByUsuario(user.id);
        setHasBabies((response.data || []).length > 0);
      } catch (error) {
        console.error('Error checking babies', error);
        setHasBabies(false);
      }
    };

    checkBabies();
  }, [babies, user]);

  if (hasBabies === false) {
    return <Navigate to="/dashboard/inicio" replace />;
  }

  if (hasBabies === null) {
    return null;
  }

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
