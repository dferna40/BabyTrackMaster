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
import IconButton from '@mui/material/IconButton';
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import HotelIcon from '@mui/icons-material/Hotel';
import BathtubIcon from '@mui/icons-material/Bathtub';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import { useNavigate } from 'react-router-dom';
import { listarRecientes } from '../../services/cuidadosService';

dayjs.extend(relativeTime);

export default function RecentCareCard() {
  const [recentCare, setRecentCare] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const navigate = useNavigate();
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const iconMap = {
    Pañal: BabyChangingStationIcon,
    Sueño: HotelIcon,
    Dormir: HotelIcon,
    Baño: BathtubIcon,
    Bañar: BathtubIcon,
    Pecho: LocalDrinkIcon,
    'Biberón': LocalDrinkIcon,
    Toma: LocalDrinkIcon,
    Alimentación: LocalDrinkIcon,
  };

  useEffect(() => {
    if (bebeId && usuarioId) {
      listarRecientes(usuarioId, bebeId, 3)
        .then((response) => setRecentCare(response.data))
        .catch((error) => console.error('Error fetching recent care:', error));
    }
  }, [bebeId, usuarioId]);

  const handleRegister = (tipoNombre) => () => {
    navigate('/dashboard/cuidados', { state: { tipo: tipoNombre } });
  };

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Actividad Reciente
        </Typography>
        {recentCare.length > 0 ? (
          <List>
            {recentCare.map((item) => {
              const Icon = iconMap[item.tipoNombre] || HelpOutlineIcon;
              const timeAgo = dayjs(item.inicio).fromNow();
              const quantity = item.cantidadMl ?? item.pecho ?? '-';
              return (
                <ListItem
                  key={item.id}
                  secondaryAction={
                    <IconButton edge="end" aria-label="registrar" onClick={handleRegister(item.tipoNombre)}>
                      <AddCircleOutlineIcon />
                    </IconButton>
                  }
                >
                  <ListItemIcon>
                    <Icon />
                  </ListItemIcon>
                  <ListItemText primary={item.tipoNombre} secondary={`${timeAgo} · ${quantity}`} />
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
