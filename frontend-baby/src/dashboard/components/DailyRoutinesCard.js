import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

const routines = [
  { time: '08:00', activity: 'Desayuno' },
  { time: '10:00', activity: 'Siesta' },
  { time: '12:30', activity: 'Almuerzo' },
  { time: '15:00', activity: 'Paseo' },
];

export default function DailyRoutinesCard() {
  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Rutinas Diarias
        </Typography>
        <List>
          {routines.map((item, index) => (
            <ListItem key={index} disableGutters>
              <ListItemText
                primary={item.activity}
                secondary={item.time}
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
