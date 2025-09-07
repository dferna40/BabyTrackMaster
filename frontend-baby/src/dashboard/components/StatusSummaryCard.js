import React, { useContext, useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import HotelIcon from '@mui/icons-material/Hotel';
import BathtubIcon from '@mui/icons-material/Bathtub';
import dayjs from 'dayjs';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { listarRecientes } from '../../services/cuidadosService';

export default function StatusSummaryCard() {
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);
  const [events, setEvents] = useState({
    feeding: null,
    diaper: null,
    sleep: null,
    bath: null,
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      listarRecientes(user.id, activeBaby.id, 20)
        .then(({ data }) => {
          const findByName = (names) =>
            data.find((item) => names.includes(item.tipoNombre));

          setEvents({
            feeding: findByName(['Pecho', 'Biberón', 'Toma', 'Alimentación']),
            diaper: findByName(['Pañal']),
            sleep: findByName(['Sueño', 'Dormir']),
            bath: findByName(['Baño', 'Bañar']),
          });
        })
        .catch(() =>
          setEvents({ feeding: null, diaper: null, sleep: null, bath: null })
        );
    }
  }, [user, activeBaby]);

  const formatTime = (event) =>
    event ? dayjs(event.inicio).format('DD/MM HH:mm') : 'Sin registro';

  const summary = [
    { title: 'Alimentación', value: formatTime(events.feeding), icon: LocalDrinkIcon },
    { title: 'Pañal', value: formatTime(events.diaper), icon: BabyChangingStationIcon },
    { title: 'Sueño', value: formatTime(events.sleep), icon: HotelIcon },
    { title: 'Baño', value: formatTime(events.bath), icon: BathtubIcon },
  ];

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Últimos cuidados
        </Typography>
        <Grid container spacing={2}>
          {summary.map((item) => (
            <Grid size={{ xs: 6, md: 3 }} key={item.title}>
              <Card variant="outlined">
                <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <item.icon color="action" />
                  <Box>
                    <Typography variant="subtitle2">{item.title}</Typography>
                    <Typography variant="body2" color="text.secondary">
                      {item.value}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </CardContent>
    </Card>
  );
}
