import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';

export default function UpcomingAppointmentsCard({ appointments = [], error }) {
  const navigate = useNavigate();

  const COLOR_BY_TIPO = {
    consulta: 'success',
    vacuna: 'warning',
    urgencia: 'error',
    // agregar más según catálogos de la API
  };

  const getTipoColor = (tipo) =>
    COLOR_BY_TIPO[tipo?.toLowerCase()] || 'default';

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          <CalendarMonthIcon sx={{ mr: 1 }} /> Próximas Citas
        </Typography>
        {error ? (
          <Typography variant="body2" color="error">
            {error}
          </Typography>
        ) : appointments.length > 0 ? (
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            {appointments.map((c) => {
              const appointmentDate = dayjs(`${c.fecha}T${c.hora}`);
              const today = dayjs();
              let formattedDate;
              if (appointmentDate.isSame(today, 'day')) {
                formattedDate = `Hoy ${appointmentDate.format('hh:mm A')}`;
              } else if (appointmentDate.isSame(today.add(1, 'day'), 'day')) {
                formattedDate = `Mañana ${appointmentDate.format('hh:mm A')}`;
              } else if (appointmentDate.isSame(today, 'week')) {
                formattedDate = appointmentDate.format('dddd hh:mm A');
                formattedDate = formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1);
              } else {
                formattedDate = appointmentDate.format('DD/MM/YYYY hh:mm A');
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
                    <Chip
                      label={c.tipoNombre || c.tipo?.nombre}
                      color={getTipoColor(c.tipoNombre || c.tipo?.nombre)}
                      size="small"
                    />
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
        <Button
          size="small"
          variant="outlined"
          fullWidth
          color="inherit"
          sx={{
            mt: 2,
            bgcolor: '#fff',
            borderColor: '#e0e6ed',
            color: 'text.primary',
            '&:hover': {
              bgcolor: '#F48872',
              borderColor: '#F48872',
              color: '#fff',
              boxShadow: '0px 4px 8px rgba(0,0,0,0.2)',
            },
          }}
          onClick={() => navigate('/dashboard/citas')}
        >
          Ver todas las citas
        </Button>
      </CardContent>
    </Card>
  );
}
