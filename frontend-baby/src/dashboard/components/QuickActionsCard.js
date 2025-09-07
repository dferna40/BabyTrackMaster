import React from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import HotelIcon from '@mui/icons-material/Hotel';
import BathtubIcon from '@mui/icons-material/Bathtub';
import { useNavigate } from 'react-router-dom';

export default function QuickActionsCard() {
  const navigate = useNavigate();

  const handleNavigate = (path, state) => () => {
    navigate(path, { state });
  };

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Grid container spacing={2}>
          <Grid item xs={6}>
            <Button
              variant="contained"
              fullWidth
              startIcon={<LocalDrinkIcon />}
              onClick={handleNavigate('/dashboard/alimentacion', { tipo: 'biberon' })}
            >
              Biberón
            </Button>
          </Grid>
          <Grid item xs={6}>
            <Button
              variant="contained"
              fullWidth
              startIcon={<BabyChangingStationIcon />}
              onClick={handleNavigate('/dashboard/cuidados', { tipo: 'Pañal' })}
            >
              Pañal
            </Button>
          </Grid>
          <Grid item xs={6}>
            <Button
              variant="contained"
              fullWidth
              startIcon={<HotelIcon />}
              onClick={handleNavigate('/dashboard/cuidados', { tipo: 'Sueño' })}
            >
              Sueño
            </Button>
          </Grid>
          <Grid item xs={6}>
            <Button
              variant="contained"
              fullWidth
              startIcon={<BathtubIcon />}
              onClick={handleNavigate('/dashboard/cuidados', { tipo: 'Baño' })}
            >
              Baño
            </Button>
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  );
}
