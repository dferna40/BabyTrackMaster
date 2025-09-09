import React, { useContext, useEffect, useState } from 'react';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import Avatar from '@mui/material/Avatar';
import { alpha } from '@mui/material/styles';
import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import BabyChangingStationIcon from '@mui/icons-material/BabyChangingStation';
import HotelIcon from '@mui/icons-material/Hotel';
import BathtubIcon from '@mui/icons-material/Bathtub';
import BiotechIcon from '@mui/icons-material/Biotech';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';
import dayjs from 'dayjs';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import {
  listarRecientes as listarCuidadosRecientes,
  obtenerStatsRapidas,
} from '../../services/cuidadosService';
import { listarRecientes as listarAlimentacionRecientes } from '../../services/alimentacionService';
import { listarRecientes as listarGastosRecientes } from '../../services/gastosService';

export default function QuickActionsCard() {
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);
  const { activeBaby } = useContext(BabyContext);

  const [actionsData, setActionsData] = useState({
    pecho: { last: null, today: 0 },
    biberon: { last: null, today: 0 },
    panal: { last: null, today: 0 },
    sueno: { last: null, today: 0 },
    bano: { last: null, today: 0 },
    gasto: { last: null, today: 0 },
  });

  useEffect(() => {
    if (user?.id && activeBaby?.id) {
      const usuarioId = user.id;
      const bebeId = activeBaby.id;

      obtenerStatsRapidas(usuarioId, bebeId)
        .then(({ data }) =>
          setActionsData((prev) => ({
            ...prev,
            panal: { ...prev.panal, today: data.panales || 0 },
            sueno: { ...prev.sueno, today: data.horasSueno || 0 },
            bano: { ...prev.bano, today: data.banos || 0 },
          }))
        )
        .catch(() => {
          /* ignore errors */
        });

      listarAlimentacionRecientes(usuarioId, bebeId, 20)
        .then(({ data }) => {
          if (Array.isArray(data) && data.length > 0) {
            const now = dayjs();
            const getDate = (item) =>
              dayjs(
                item.inicio ||
                  item.fecha ||
                  item.date ||
                  item.fechaHora ||
                  item.createdAt,
              );

            const pechoItems = data.filter((item) => item.tipo === 'lactancia');
            const biberonItems = data.filter((item) => item.tipo === 'biberon');

            const lastPecho = pechoItems[0] ? getDate(pechoItems[0]) : null;
            const lastBiberon = biberonItems[0]
              ? getDate(biberonItems[0])
              : null;

            const todayPecho = pechoItems.filter((item) =>
              getDate(item).isSame(now, 'day'),
            ).length;
            const todayBiberon = biberonItems.filter((item) =>
              getDate(item).isSame(now, 'day'),
            ).length;

            setActionsData((prev) => ({
              ...prev,
              pecho: {
                last: lastPecho || prev.pecho.last,
                today: todayPecho,
              },
              biberon: {
                last: lastBiberon || prev.biberon.last,
                today: todayBiberon,
              },
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

      listarGastosRecientes(usuarioId, bebeId, 20)
        .then(({ data }) => {
          if (Array.isArray(data) && data.length > 0) {
            const now = dayjs();
            const getDate = (item) =>
              dayjs(
                item.fecha ||
                  item.inicio ||
                  item.date ||
                  item.fechaHora ||
                  item.createdAt,
              );
            const last = getDate(data[0]);
            const todayTotal = data.reduce((sum, item) => {
              const date = getDate(item);
              return date.isSame(now, 'day')
                ? sum + Number(item.cantidad || 0)
                : sum;
            }, 0);
            setActionsData((prev) => ({
              ...prev,
              gasto: {
                last: last || prev.gasto.last,
                today: todayTotal,
              },
            }));
          }
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
      key: 'pecho',
      label: 'Pecho',
      icon: BiotechIcon,
      path: '/dashboard/alimentacion',
      state: {
        tipo: 'lactancia',
        tipoLactancia: 'Lactancia directa',
        disableTipo: true,
        disableTipoLactancia: true,
      },
      unit: '',
      color: 'primary',
    },
    {
      key: 'biberon',
      label: 'Biberón',
      icon: LocalDrinkIcon,
      path: '/dashboard/alimentacion',
      state: { tipo: 'biberon', disableTipo: true },
      unit: '',
      color: 'primary',
    },
    {
      key: 'panal',
      label: 'Pañal',
      icon: BabyChangingStationIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Pañal', disableTipo: true },
      unit: '',
      color: 'success',
    },
    {
      key: 'bano',
      label: 'Baño',
      icon: BathtubIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Baño', disableTipo: true },
      unit: '',
      color: 'info',
    },
    {
      key: 'sueno',
      label: 'Sueño',
      icon: HotelIcon,
      path: '/dashboard/cuidados',
      state: { tipo: 'Sueño', disableTipo: true },
      unit: 'h',
      color: 'secondary',
    },
    {
      key: 'gasto',
      label: 'Gasto',
      icon: AttachMoneyIcon,
      path: '/dashboard/gastos',
      state: { openForm: true },
      unit: '',
      color: 'warning',
    },
  ];

  return (
    <Card
      sx={{
        height: '100%',
        boxShadow: 3,
        borderRadius: 2,
        bgcolor: (theme) =>
          theme.vars
            ? `rgba(${theme.vars.palette.background.paperChannel} / 1)`
            : theme.palette.background.paper,
      }}
    >
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
                <Card
                  sx={{
                    height: '100%',
                    boxShadow: 1,
                    borderRadius: 2,
                    bgcolor: (theme) =>
                      theme.vars
                        ? `rgba(${theme.vars.palette.background.paperChannel} / 1)`
                        : theme.palette.background.paper,
                  }}
                >
                  <CardContent>
                    <Stack spacing={1} alignItems="center">
                      <Avatar
                        sx={{
                          bgcolor: (theme) =>
                            alpha(theme.palette[action.color].main, 0.15),
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
