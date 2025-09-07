import React, { useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
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
          Próximas Citas
        </Typography>
        {appointments.length > 0 ? (
          <List>
            {appointments.map((c) => (
              <ListItem
                key={c.id}
                disableGutters
                secondaryAction={
                  <Chip
                    label={c.tipoNombre}
                    color={getTipoColor(c.tipoNombre)}
                    size="small"
                  />
                }
              >
                <ListItemText
                  primary={dayjs(`${c.fecha}T${c.hora}`).format('DD/MM/YYYY HH:mm')}
                  secondary={`${c.motivo} · ${c.centroMedico} · ${c.estadoNombre}`}
                  primaryTypographyProps={{ variant: 'body2' }}
                  secondaryTypographyProps={{
                    variant: 'caption',
                    color: 'text.secondary',
                  }}
                />
              </ListItem>
            ))}
          </List>
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
