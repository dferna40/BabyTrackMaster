import * as React from 'react';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import { useTheme } from '@mui/material/styles';
import DarkModeOutlined from '@mui/icons-material/DarkModeOutlined';
import LightModeOutlined from '@mui/icons-material/LightModeOutlined';
import { ColorModeContext } from './AppTheme';

export default function ColorModeSelect(props) {
  const theme = useTheme();
  const colorMode = React.useContext(ColorModeContext);

  return (
    <Tooltip title="Toggle light/dark mode">
      <IconButton color="inherit" onClick={colorMode.toggleColorMode} {...props}>
        {theme.palette.mode === 'dark' ? <LightModeOutlined /> : <DarkModeOutlined />}
      </IconButton>
    </Tooltip>
  );
}
