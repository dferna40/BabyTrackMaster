import React, { useContext } from 'react';
import Stack from '@mui/material/Stack';
import Avatar from '@mui/material/Avatar';
import NotificationsRoundedIcon from '@mui/icons-material/NotificationsRounded';
import { Link } from 'react-router-dom';
import MenuButton from './MenuButton';
import ColorModeIconDropdown from '../../shared-theme/ColorModeIconDropdown';
import { BabyContext } from '../../context/BabyContext';

export default function Header() {
  const { activeBaby } = useContext(BabyContext);
  return (
    <Stack
      direction="row"
      sx={{
        display: { xs: 'none', md: 'flex' },
        width: '100%',
        alignItems: { xs: 'flex-start', md: 'center' },
        justifyContent: 'flex-end',
        maxWidth: { sm: '100%', md: '1700px' },
        pt: 1.5,
        gap: 1,
      }}
    >
      <MenuButton showBadge>
        <NotificationsRoundedIcon />
      </MenuButton>
      <ColorModeIconDropdown />
      <Avatar
        component={Link}
        to="/dashboard/editar-bebe"
        src={
          activeBaby?.imagenBebe
            ? `data:image/*;base64,${activeBaby.imagenBebe}`
            : undefined
        }
      >
        {!activeBaby?.imagenBebe &&
          activeBaby?.nombre?.[0]?.toUpperCase()}
      </Avatar>
    </Stack>
  );
}
