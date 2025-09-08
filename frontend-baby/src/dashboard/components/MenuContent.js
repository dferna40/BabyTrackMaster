import * as React from 'react';
import { NavLink } from 'react-router-dom';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Stack from '@mui/material/Stack';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import MedicalServicesRoundedIcon from '@mui/icons-material/MedicalServicesRounded';
import MonetizationOnRoundedIcon from '@mui/icons-material/MonetizationOnRounded';
import MenuBookRoundedIcon from '@mui/icons-material/MenuBookRounded';
import RamenDiningRoundedIcon from '@mui/icons-material/RamenDiningRounded';
import EventRoundedIcon from '@mui/icons-material/EventRounded';
import ScheduleRoundedIcon from '@mui/icons-material/ScheduleRounded';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import InfoRoundedIcon from '@mui/icons-material/InfoRounded';

const mainListItems = [
  { text: 'Inicio', icon: <HomeRoundedIcon />, to: '/dashboard' },
  { text: 'Cuidados', icon: <MedicalServicesRoundedIcon />, to: '/dashboard/cuidados' },
  { text: 'Gastos', icon: <MonetizationOnRoundedIcon />, to: '/dashboard/gastos' },
  { text: 'Diario', icon: <MenuBookRoundedIcon />, to: '/dashboard/diario' },
  { text: 'Alimentación', icon: <RamenDiningRoundedIcon />, to: '/dashboard/alimentacion' },
  { text: 'Citas', icon: <EventRoundedIcon />, to: '/dashboard/citas' },
  { text: 'Rutinas', icon: <ScheduleRoundedIcon />, to: '/dashboard/rutinas' },
];

const secondaryListItems = [
  { text: 'Añadir bebe', icon: <AddCircleRoundedIcon />, to: '/dashboard/anadir-bebe' },
  { text: 'Editar/borrar bebé', icon: <SettingsRoundedIcon />, to: '/dashboard/editar-bebe' },
  { text: 'Acerca de', icon: <InfoRoundedIcon />, to: '/dashboard/acerca' },
];

export default function MenuContent() {
  return (
    <Stack sx={{ flexGrow: 1, p: 1, justifyContent: 'space-between' }}>
      <List dense>
        {mainListItems.map((item, index) => (
          <ListItem key={index} disablePadding sx={{ display: 'block' }}>
            <NavLink
              to={item.to}
              {...(item.to === '/dashboard' ? { end: true } : {})}
              style={{ textDecoration: 'none', color: 'inherit' }}
            >
              {({ isActive }) => (
                <ListItemButton selected={isActive}>
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItemButton>
              )}
            </NavLink>
          </ListItem>
        ))}
      </List>
      <List dense>
        {secondaryListItems.map((item, index) => (
          <ListItem key={index} disablePadding sx={{ display: 'block' }}>
            <NavLink
              to={item.to}
              {...(item.to === '/dashboard' ? { end: true } : {})}
              style={{ textDecoration: 'none', color: 'inherit' }}
            >
              {({ isActive }) => (
                <ListItemButton selected={isActive}>
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItemButton>
              )}
            </NavLink>
          </ListItem>
        ))}
      </List>
    </Stack>
  );
}
