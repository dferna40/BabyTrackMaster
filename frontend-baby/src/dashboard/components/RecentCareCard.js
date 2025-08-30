import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

const careEntries = [
  { time: '08:30', action: 'Biberón 120ml' },
  { time: '09:15', action: 'Cambio de pañal' },
  { time: '11:00', action: 'Siesta' },
];

export default function RecentCareCard() {
  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Cuidados Recientes
        </Typography>
        <List>
          {careEntries.map((item, index) => (
            <ListItem key={index} disableGutters>
              <ListItemText
                primary={item.action}
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
