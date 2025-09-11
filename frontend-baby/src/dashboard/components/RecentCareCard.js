import React, { useEffect, useState } from 'react';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Box from '@mui/material/Box';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import { listarRecientes } from '../../services/cuidadosService';

dayjs.extend(utc);
dayjs.extend(timezone);

export default function RecentCareCard() {
  const [recentCare, setRecentCare] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const iconMap = {
    Pañal: '👶',
    Sueño: '😴',
    Dormir: '😴',
    Baño: '🛁',
    Bañar: '🛁',
    Pecho: '🍼',
    'Biberón': '🍼',
    Toma: '🍼',
    Alimentación: '🍼',
  };

  const formatTimeAgo = (inicio) => {
    const diffMinutes = dayjs().diff(dayjs.utc(inicio).local(), 'minute');
    const hours = Math.floor(diffMinutes / 60);
    const minutes = diffMinutes % 60;
    const hoursStr = hours ? `${hours}h ` : '';
    return `Hace ${hoursStr}${minutes}m`;
  };

  const formatValue = (item) => {
    switch (item.tipoNombre) {
      case 'Biberón':
      case 'Pecho':
      case 'Toma':
      case 'Alimentación': {
        const quantity = item.cantidadMl;
        return quantity ? `${quantity}ml` : '';
      }
      case 'Pañal':
        return item.tipoPanalNombre || '';
      case 'Sueño':
      case 'Dormir': {
        if (item.duracion != null) {
          return item.duracion;
        }
        const durationMin =
          item.fin
            ? dayjs
                .utc(item.fin)
                .local()
                .diff(dayjs.utc(item.inicio).local(), 'minute')
            : 0;
        const hours = Math.floor(durationMin / 60);
        const minutes = durationMin % 60;
        return `${hours}h ${minutes}m`;
      }
      case 'Baño':
      case 'Bañar':
        return 'Limpio';
      default:
        return '';
    }
  };

  useEffect(() => {
    if (bebeId && usuarioId) {
      listarRecientes(usuarioId, bebeId, 3)
        .then((response) => {
          const cutoff = dayjs().subtract(120, 'hour');
          const filtered = response.data.filter((cuidado) =>
            dayjs(cuidado.inicio).isAfter(cutoff)
          );
          setRecentCare(filtered);
        })
        .catch((error) => console.error('Error fetching recent care:', error));
    }
  }, [bebeId, usuarioId]);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography
          variant="h6"
          component="h2"
          gutterBottom
          sx={{ display: 'flex', alignItems: 'center', gap: 1 }}
        >
          <span role="img" aria-label="reloj">
            ⏰
          </span>
          Actividad Reciente
        </Typography>
        {recentCare.length > 0 ? (
          <List sx={{ p: 0 }}>
            {recentCare.map((item) => {
              const emoji = iconMap[item.tipoNombre] || '❓';
              const timeAgo = formatTimeAgo(item.inicio);
              const value = formatValue(item);
              return (
                <ListItem
                  key={item.id}
                  sx={{
                    border: '1px solid',
                    borderColor: 'divider',
                    borderRadius: 2,
                    mb: 1,
                  }}
                >
                  <ListItemIcon sx={{ minWidth: 40 }}>
                    <Typography component="span" fontSize="1.5rem">
                      {emoji}
                    </Typography>
                  </ListItemIcon>
                  <ListItemText primary={item.tipoNombre} secondary={timeAgo} />
                  <Box>
                    <Typography
                      variant="body2"
                      color="primary"
                      sx={{ fontWeight: 'bold' }}
                    >
                      {value}
                    </Typography>
                  </Box>
                </ListItem>
              );
            })}
          </List>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No hay actividades recientes.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
}
