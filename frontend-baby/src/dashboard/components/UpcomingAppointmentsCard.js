import React, { useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import { BabyContext } from '../../context/BabyContext';
import { listarProximas } from '../../services/citasService';

export default function UpcomingAppointmentsCard() {
  const [appointments, setAppointments] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!activeBaby?.id) return;
    listarProximas(activeBaby.id)
      .then((data) => {
        setAppointments(data);
      })
      .catch((err) => {
        console.error('Error fetching citas:', err);
        setAppointments([]);
      });
  }, [activeBaby]);

  const getTipoColor = (tipo) => {
    switch (tipo?.toLowerCase()) {
      case 'consulta':
        return 'primary';
      case 'vacuna':
        return 'secondary';
      default:
        return 'default';
    }
  };

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          <CalendarMonthIcon sx={{ mr: 1 }} /> Próximas Citas
        </Typography>
        {appointments.length > 0 ? (
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            {appointments.map((c) => {
              const appointmentDate = dayjs(`${c.fecha}T${c.hora}`);
              const today = dayjs();
              let formattedDate;
              if (appointmentDate.isSame(today, 'day')) {
                formattedDate = 'Hoy';
              } else if (appointmentDate.diff(today, 'day') === 1) {
                formattedDate = 'Mañana';
              } else {
                formattedDate = appointmentDate.format('DD/MM/YYYY HH:mm');
              }
              return (
                <Card key={c.id} variant="outlined" sx={{ bgcolor: 'success.light', mb: 1 }}>
                  <CardContent sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="body1">{c.motivo}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {formattedDate}
                      </Typography>
                    </Box>
                    <Chip label={c.tipoNombre} color={getTipoColor(c.tipoNombre)} size="small" />
                  </CardContent>
                </Card>
              );
            })}
          </Box>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No hay citas próximas.
          </Typography>
        )}
        <Button size="small" sx={{ mt: 2 }} onClick={() => navigate('/dashboard/citas')}>
          Ver todas las citas
        </Button>
      </CardContent>
    </Card>
  );
}
