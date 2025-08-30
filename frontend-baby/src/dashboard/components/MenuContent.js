import * as React from 'react';
import { NavLink } from 'react-router-dom';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Stack from '@mui/material/Stack';
import HomeRoundedIcon from '@mui/icons-material/HomeRounded';
import FavoriteRoundedIcon from '@mui/icons-material/FavoriteRounded';
import MonetizationOnRoundedIcon from '@mui/icons-material/MonetizationOnRounded';
import MenuBookRoundedIcon from '@mui/icons-material/MenuBookRounded';
import EventRoundedIcon from '@mui/icons-material/EventRounded';
import ScheduleRoundedIcon from '@mui/icons-material/ScheduleRounded';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import InfoRoundedIcon from '@mui/icons-material/InfoRounded';
import HelpRoundedIcon from '@mui/icons-material/HelpRounded';

const mainListItems = [
  { text: 'Inicio', icon: <HomeRoundedIcon />, to: '/dashboard' },
  { text: 'Cuidados', icon: <FavoriteRoundedIcon />, to: '/dashboard/cuidados' },
  { text: 'Gastos', icon: <MonetizationOnRoundedIcon />, to: '/dashboard/gastos' },
  { text: 'Diario', icon: <MenuBookRoundedIcon />, to: '/dashboard/diario' },
  { text: 'Citas', icon: <EventRoundedIcon />, to: '/dashboard/citas' },
  { text: 'Rutinas', icon: <ScheduleRoundedIcon />, to: '/dashboard/rutinas' },
];

const secondaryListItems = [
  { text: 'Perfil', icon: <AccountCircleIcon />, to: '/dashboard/profile' },
  { text: 'Configuración', icon: <SettingsRoundedIcon />, to: '/dashboard/configuracion' },
  { text: 'Acerca de', icon: <InfoRoundedIcon />, to: '/dashboard/acerca' },
  { text: 'Ayuda', icon: <HelpRoundedIcon />, to: '/dashboard/ayuda' },
];

export default function MenuContent() {
  return (
    <Stack sx={{ flexGrow: 1, p: 1, justifyContent: 'space-between' }}>
      <List dense>
        {mainListItems.map((item, index) => (
          <ListItem key={index} disablePadding sx={{ display: 'block' }}>
            <NavLink
              to={item.to}
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
