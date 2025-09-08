import React, { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Chip from '@mui/material/Chip';
import InputAdornment from '@mui/material/InputAdornment';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import SearchIcon from '@mui/icons-material/Search';
import dayjs from 'dayjs';
import { DatePicker } from '@mui/x-date-pickers';
import { listarEntradas, crearEntrada } from '../../services/diarioService';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';
import { saveButton } from '../../theme/buttonStyles';

const emociones = [
  { value: 'happy', label: '😊 Feliz' },
  { value: 'neutral', label: '😐 Neutral' },
  { value: 'sad', label: '😢 Triste' },
  { value: 'angry', label: '😠 Enojado' },
];

export default function Diario() {
  const [texto, setTexto] = useState('');
  const [emocion, setEmocion] = useState('neutral');
  const [fecha, setFecha] = useState(dayjs());
  const [etiqueta, setEtiqueta] = useState('');
  const [etiquetas, setEtiquetas] = useState([]);
  const [entradas, setEntradas] = useState([]);
  const [search, setSearch] = useState('');
  const [filtroEmocion, setFiltroEmocion] = useState('');
  const [filtroFecha, setFiltroFecha] = useState(null);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);

  useEffect(() => {
    if (!activeBaby || !user) return;
    listarEntradas(user.id, activeBaby.id)
      .then((data) => setEntradas(data))
      .catch((err) => console.error('Error fetching diario:', err));
  }, [activeBaby, user]);

  const handleAdd = () => {
    if (!texto.trim() || !activeBaby || !user) return;
    const payload = {
      bebeId: activeBaby.id,
      fecha: fecha.format('YYYY-MM-DD'),
      hora: dayjs().format('HH:mm'),
      titulo: texto.substring(0, 120) || 'Entrada',
      contenido: texto,
      estadoAnimo: emocion,
      etiquetas: etiquetas.join(','),
    };
    crearEntrada(user.id, payload)
      .then(() => {
        setTexto('');
        setEmocion('neutral');
        setFecha(dayjs());
        setEtiquetas([]);
        return listarEntradas(user.id, activeBaby.id).then((data) =>
          setEntradas(data)
        );
      })
      .catch((err) => console.error('Error creating entrada:', err));
  };

  const addEtiqueta = (e) => {
    if (e.key === 'Enter' && etiqueta.trim()) {
      setEtiquetas([...etiquetas, etiqueta.trim()]);
      setEtiqueta('');
    }
  };

  const entradasFiltradas = entradas.filter(
    (e) =>
      (!filtroEmocion || e.emocion === filtroEmocion) &&
      (!filtroFecha || dayjs(e.fecha).isSame(filtroFecha, 'day')) &&
      e.texto.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <Box
        sx={{
          width: '100%',
          display: 'grid',
          gap: 2,
          gridTemplateColumns: { xs: '1fr', md: '1fr 2fr' },
        }}
      >
        <Card variant="outlined">
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Añadir entrada
            </Typography>
            <Stack spacing={2}>
            <TextField
              label="Texto"
              multiline
              rows={3}
              value={texto}
              onChange={(e) => setTexto(e.target.value)}
            />
            <TextField
              select
              label="Emoción"
              value={emocion}
              onChange={(e) => setEmocion(e.target.value)}
            >
              {emociones.map((opt) => (
                <MenuItem key={opt.value} value={opt.value}>
                  {opt.label}
                </MenuItem>
              ))}
            </TextField>
            <DatePicker
              label="Fecha"
              value={fecha}
              onChange={(newValue) => setFecha(newValue)}
              slotProps={{ textField: { InputLabelProps: { shrink: true } } }}
            />
            <TextField
              label="Etiquetas"
              value={etiqueta}
              onChange={(e) => setEtiqueta(e.target.value)}
              onKeyDown={addEtiqueta}
              helperText="Presiona Enter para añadir"
            />
            <Stack direction="row" spacing={1}>
              {etiquetas.map((t) => (
                <Chip
                  key={t}
                  label={t}
                  onDelete={() =>
                    setEtiquetas(etiquetas.filter((x) => x !== t))
                  }
                />
              ))}
            </Stack>
              <Button
                variant="contained"
                onClick={handleAdd}
                sx={saveButton}
              >
                Guardar entrada
              </Button>
            </Stack>
          </CardContent>
        </Card>

        <Box>
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} mb={2}>
          <TextField
            select
            label="Emoción"
            value={filtroEmocion}
            onChange={(e) => setFiltroEmocion(e.target.value)}
            sx={{ minWidth: 120 }}
          >
            <MenuItem value="">Todas</MenuItem>
            {emociones.map((opt) => (
              <MenuItem key={opt.value} value={opt.value}>
                {opt.label}
              </MenuItem>
            ))}
          </TextField>
          <DatePicker
            label="Fecha"
            value={filtroFecha}
            onChange={(newValue) => setFiltroFecha(newValue)}
            slotProps={{ textField: { InputLabelProps: { shrink: true } } }}
          />
          <TextField
            placeholder="Buscar"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              ),
            }}
          />
          </Stack>

          <Card variant="outlined">
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Entradas del diario
              </Typography>
              <List>
                {entradasFiltradas.map((e) => (
                  <ListItem key={e.id} alignItems="flex-start">
                    <ListItemText
                      primary={`${
                        emociones.find((em) => em.value === e.emocion)?.label ?? ''
                      } ${dayjs(e.fecha).format('DD/MM/YYYY')}`}
                      secondary={
                        <>
                          <Typography component="span">{e.texto}</Typography>
                          <Box sx={{ mt: 1 }}>
                            {e.etiquetas?.map((t) => (
                              <Chip key={t} size="small" label={t} sx={{ mr: 1 }} />
                            ))}
                          </Box>
                        </>
                      }
                    />
                  </ListItem>
                ))}
                {!entradasFiltradas.length && (
                  <Typography variant="body2">Sin entradas</Typography>
                )}
              </List>
            </CardContent>
          </Card>
        </Box>
      </Box>
  );
}

