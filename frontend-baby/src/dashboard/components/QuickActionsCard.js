import React, { useContext, useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Avatar from '@mui/material/Avatar';
import { lightBlue, lightGreen, deepPurple, cyan } from '@mui/material/colors';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import HotelIcon from '@mui/icons-material/Hotel';
import BathtubIcon from '@mui/icons-material/Bathtub';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import {
  listarRecientes as listarCuidadosRecientes,
  obtenerStatsRapidas,
} from '../../services/cuidadosService';
import { listarRecientes as listarAlimentacionRecientes } from '../../services/alimentacionService';

export default function QuickActionsCard() {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);

  const [actionsData, setActionsData] = useState({
    biberon: { last: null, today: 0 },
    panal: { last: null, today: 0 },
    sueno: { last: null, today: 0 },
    bano: { last: null, today: 0 },
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      const usuarioId = user.id;
      const bebeId = activeBaby.id;

      obtenerStatsRapidas(usuarioId, bebeId)
        .then(({ data }) =>
          setActionsData((prev) => ({
            biberon: { ...prev.biberon, today: data.tomas || 0 },
            panal: { ...prev.panal, today: data.panales || 0 },
            sueno: { ...prev.sueno, today: data.horasSueno || 0 },
            bano: { ...prev.bano, today: data.banos || 0 },
          }))
        )
        .catch(() => {
          /* ignore errors */
        });

      listarAlimentacionRecientes(usuarioId, bebeId, 1)
        .then(({ data }) => {
          if (Array.isArray(data) && data.length > 0) {
            const last = data[0];
            const date = dayjs(
              last.inicio ||
                last.fecha ||
                last.date ||
                last.fechaHora ||
                last.createdAt
            );
            setActionsData((prev) => ({
              ...prev,
              biberon: { ...prev.biberon, last: date },
            }));
          }
        })
        .catch(() => {
          /* ignore errors */
        });

      listarCuidadosRecientes(usuarioId, bebeId, 20)
        .then(({ data }) => {
          const updates = {};
          data.forEach((item) => {
            if (item.tipoNombre === 'Pañal' && !updates.panal) {
              updates.panal = dayjs(item.inicio);
            }
            if (item.tipoNombre === 'Sueño' && !updates.sueno) {
              updates.sueno = dayjs(item.inicio);
            }
            if (item.tipoNombre === 'Baño' && !updates.bano) {
              updates.bano = dayjs(item.inicio);
            }
          });
          setActionsData((prev) => ({
            ...prev,
            panal: { ...prev.panal, last: updates.panal || prev.panal.last },
            sueno: { ...prev.sueno, last: updates.sueno || prev.sueno.last },
            bano: { ...prev.bano, last: updates.bano || prev.bano.last },
          }));
        })
        .catch(() => {
          /* ignore errors */
        });
    }
  }, [user, activeBaby]);

  const handleNavigate = (path, state) => () => {
    navigate(path, { state });
  };

  const actions = [
    {
      key: 'biberon',
      label: 'Biberón',
      icon: LocalDrinkIcon,
      path: '/dashboard/alimentacion',
      state: { tipo: 'biberon' },
      unit: '',
      color: 'primary',
      bgColor: lightBlue[50],
    },
    {
      key: 'panal',
      label: 'Pañal',
      icon: BabyChangingStationIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Pañal' },
      unit: '',
      color: 'success',
      bgColor: lightGreen[50],
    },
    {
      key: 'sueno',
      label: 'Sueño',
      icon: HotelIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Sueño' },
      unit: 'h',
      color: 'secondary',
      bgColor: deepPurple[50],
    },
    {
      key: 'bano',
      label: 'Baño',
      icon: BathtubIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Baño' },
      unit: '',
      color: 'info',
      bgColor: cyan[50],
    },
  ];

  return (
    <Card sx={{ height: '100%', boxShadow: 3, borderRadius: 2, bgcolor: '#F3F8FF' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Acciones Rápidas
        </Typography>
        <Grid container spacing={2}>
          {actions.map((action) => {
            const Icon = action.icon;
            const info = actionsData[action.key];
            const last = info.last ? info.last.fromNow() : '-';
            const today = `${info.today}${action.unit}`;

            return (
              <Grid item xs={6} md={3} key={action.key}>
                <Card sx={{ height: '100%', boxShadow: 1, borderRadius: 2, bgcolor: '#F3F8FF' }}>
                  <CardContent>
                    <Stack spacing={1} alignItems="center">
                      <Avatar
                        sx={{
                          bgcolor: action.bgColor,
                          color: (theme) => theme.palette[action.color].main,
                        }}
                      >
                        <Icon />
                      </Avatar>
                      <Typography variant="subtitle1">{action.label}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        Última vez: {last}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        Hoy: {today}
                      </Typography>
                      <Button
                        variant="contained"
                        size="small"
                        fullWidth
                        onClick={handleNavigate(action.path, action.state)}
                      >
                        Registrar {action.label}
                      </Button>
                    </Stack>
                  </CardContent>
                </Card>
              </Grid>
            );
          })}
        </Grid>
      </CardContent>
    </Card>
  );
}
