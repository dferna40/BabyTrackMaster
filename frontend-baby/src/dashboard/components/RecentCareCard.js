import React, { useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import { listarRecientes } from '../../services/cuidadosService';

export default function RecentCareCard() {
  const [recentCare, setRecentCare] = useState([]);
  const bebeId = 1;

  useEffect(() => {
    listarRecientes(bebeId)
      .then((response) => setRecentCare(response.data))
      .catch((error) => console.error('Error fetching recent care:', error));
  }, [bebeId]);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Cuidados Recientes
        </Typography>
        <List>
          {recentCare.map((item) => (
            <ListItem key={item.id} disableGutters>
              <ListItemText
                primary={item.tipoNombre}
                secondary={item.inicio}
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
