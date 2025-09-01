import React, { useEffect, useState } from 'react';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { listarRecientes } from '../../services/cuidadosService';

export default function RecentCareCard() {
  const [recentCare, setRecentCare] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  useEffect(() => {
    if (bebeId && usuarioId) {
      listarRecientes(usuarioId, bebeId)
        .then((response) => setRecentCare(response.data))
        .catch((error) => console.error('Error fetching recent care:', error));
    }
  }, [bebeId, usuarioId]);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Cuidados Recientes
        </Typography>
        <List>
          {recentCare.slice(0, 5).map((item) => (
            <ListItem key={item.id} disableGutters>
              <ListItemText
                primary={item.tipoNombre}
                secondary={dayjs(item.inicio).locale('es').format('DD/MM/YYYY HH:mm')}
                primaryTypographyProps={{ variant: 'body2' }}
                secondaryTypographyProps={{ variant: 'caption', color: 'text.secondary' }}
              />
            </ListItem>
          ))}
        </List>
      </CardContent>
    </Card>
  );
}
