import * as React from 'react';
import MuiAvatar from '@mui/material/Avatar';
import MuiListItemAvatar from '@mui/material/ListItemAvatar';
import MenuItem from '@mui/material/MenuItem';
import ListItemText from '@mui/material/ListItemText';
import Select, { selectClasses } from '@mui/material/Select';
import { styled } from '@mui/material/styles';
import { BabyContext } from '../../context/BabyContext';

const Avatar = styled(MuiAvatar)(({ theme }) => ({
  width: 28,
  height: 28,
  backgroundColor: (theme.vars || theme).palette.background.paper,
  color: (theme.vars || theme).palette.text.secondary,
  border: `1px solid ${(theme.vars || theme).palette.divider}`,
}));

const ListItemAvatar = styled(MuiListItemAvatar)({
  minWidth: 0,
  marginRight: 12,
});

export default function SelectContent() {
  const { babies, activeBaby, setActiveBaby } = React.useContext(BabyContext);

  const handleChange = (event) => {
    const selectedId = parseInt(event.target.value, 10);
    const baby = babies.find((b) => b.id === selectedId);
    if (baby) {
      setActiveBaby(baby);
    }
  };

  return (
    <Select
      labelId="baby-select"
      id="baby-select"
      value={activeBaby?.id.toString() || ''}
      onChange={handleChange}
      displayEmpty
      inputProps={{ 'aria-label': 'Seleccionar bebé' }}
      fullWidth
      sx={{
        maxHeight: 56,
        width: 215,
        '&.MuiList-root': {
          p: '8px',
        },
        [`& .${selectClasses.select}`]: {
          display: 'flex',
          alignItems: 'center',
          gap: '2px',
          pl: 1,
        },
      }}
      renderValue={(selected) => {
        const baby = babies.find((b) => b.id.toString() === selected);
        return baby ? baby.nombre : 'Seleccionar bebé';
      }}
    >
      {babies.map((baby) => (
        <MenuItem key={baby.id} value={baby.id.toString()}>
          <ListItemAvatar>
            <Avatar alt={baby.nombre}>{baby.nombre.charAt(0)}</Avatar>
          </ListItemAvatar>
          <ListItemText
            primary={baby.nombre}
            secondary={`Nacimiento: ${baby.fechaNacimiento}`}
          />
        </MenuItem>
      ))}
    </Select>
  );
}
