import * as React from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import AutoFixHighRoundedIcon from '@mui/icons-material/AutoFixHighRounded';
import ConstructionRoundedIcon from '@mui/icons-material/ConstructionRounded';
import SettingsSuggestRoundedIcon from '@mui/icons-material/SettingsSuggestRounded';
import ThumbUpAltRoundedIcon from '@mui/icons-material/ThumbUpAltRounded';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import ChildCareIcon from '@mui/icons-material/ChildCare';
import { SitemarkIcon } from './CustomIcons';

const items = [
  {
    icon: <ChildCareIcon sx={{ color: 'text.secondary' }} />,
    title: 'BabyTrackMaster',
    description:
      'Organiza y acompaña cada momento de tu bebé.',
  },
  {
    icon: <BabyChangingStationIcon sx={{ color: 'text.secondary' }} />,
    title: 'Experimenta una gestión integral de tu bebé',
    description:
      'Desde el seguimiento de la alimentación y el sueño hasta el control del crecimiento y desarrollo, todo en una sola aplicación.',
  },
  {
    icon: <ThumbUpAltRoundedIcon sx={{ color: 'text.secondary' }} />,
    title: 'Gran experiencia de usuario',
    description:
      'Disfruta de una interfaz intuitiva y atractiva que hace que el seguimiento del desarrollo de tu bebé sea sencillo y agradable.',
  },
  {
    icon: <AutoFixHighRoundedIcon sx={{ color: 'text.secondary' }} />,
    title: 'Funcionalidad innovadora',
    description:
      'Aprovecha las últimas tecnologías para ofrecerte herramientas avanzadas que facilitan la gestión y el seguimiento del bienestar de tu bebé.',
  },
];

export default function Content() {
  return (
    <Stack
      sx={{ flexDirection: 'column', alignSelf: 'center', gap: 4, maxWidth: 450 }}
    >
      <Box sx={{ display: { xs: 'none', md: 'flex' }, justifyContent: 'center', width: '100%' }}>
        <SitemarkIcon />
      </Box>
      {items.map((item, index) => (
        <Stack key={index} direction="row" sx={{ gap: 2 }}>
          {item.icon}
          <div>
            <Typography gutterBottom sx={{ fontWeight: 'medium' }}>
              {item.title}
            </Typography>
            <Typography variant="body2" sx={{ color: 'text.secondary' }}>
              {item.description}
            </Typography>
          </div>
        </Stack>
      ))}
    </Stack>
  );
}
