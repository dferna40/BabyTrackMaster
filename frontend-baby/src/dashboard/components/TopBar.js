import * as React from 'react';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import NotificationsRoundedIcon from '@mui/icons-material/NotificationsRounded';
import SettingsRoundedIcon from '@mui/icons-material/SettingsRounded';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';
import MenuButton from './MenuButton';

export default function TopBar() {
  return (
    <Stack direction="row" spacing={2} alignItems="center">
      <Typography variant="h6" sx={{ fontWeight: 600 }}>
        BabyTrackMaster
      </Typography>
      <Stack direction="row" spacing={1} sx={{ ml: 2 }}>
        <MenuButton showBadge aria-label="Abrir notificaciones">
          <NotificationsRoundedIcon />
        </MenuButton>
        <MenuButton aria-label="Abrir configuración">
          <SettingsRoundedIcon />
        </MenuButton>
        <MenuButton aria-label="Abrir perfil">
          <PersonRoundedIcon />
        </MenuButton>
      </Stack>
    </Stack>
  );
}
