import React, { useEffect, useMemo, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';

import {
  listarPorBebe,
  crearRutina,
  actualizarRutina,
  eliminarRutina,
  duplicarRutina,
} from '../../services/rutinasService';
import RutinaForm from '../components/RutinaForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

const diasOptions = [
  { value: 'L', label: 'Lunes' },
  { value: 'M', label: 'Martes' },
  { value: 'X', label: 'Miércoles' },
  { value: 'J', label: 'Jueves' },
  { value: 'V', label: 'Viernes' },
  { value: 'S', label: 'Sábado' },
  { value: 'D', label: 'Domingo' },
];

const tipoOptions = [
  { value: 'Alimentación', label: 'Alimentación' },
  { value: 'Juego', label: 'Juego' },
  { value: 'Sueño', label: 'Sueño' },
  { value: 'Baño', label: 'Baño' },
];

const dayMap = diasOptions.reduce((acc, d) => ({ ...acc, [d.value]: d.label }), {});

export default function Rutinas() {
  const [rutinas, setRutinas] = useState([]);
  const [search, setSearch] = useState('');
  const [dayFilter, setDayFilter] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedRutina, setSelectedRutina] = useState(null);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const fetchRutinas = () => {
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId)
      .then((response) => {
        setRutinas(response.data.content ?? response.data);
      })
      .catch((error) => {
        console.error('Error fetching rutinas:', error);
      });
  };

  useEffect(() => {
    if (bebeId) {
      fetchRutinas();
    }
  }, [bebeId]);

  const filteredRutinas = useMemo(() => {
    return rutinas.filter((r) => {
      const matchSearch = search
        ? r.tipo?.toLowerCase().includes(search.toLowerCase())
        : true;
      const matchDay = dayFilter ? r.dia === dayFilter : true;
      const matchType = typeFilter ? r.tipo === typeFilter : true;
      return matchSearch && matchDay && matchType;
    });
  }, [rutinas, search, dayFilter, typeFilter]);

  const handleAdd = () => {
    setSelectedRutina(null);
    setOpenForm(true);
  };

  const handleEdit = (rutina) => {
    setSelectedRutina(rutina);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId || !usuarioId) return;
    if (window.confirm('¿Eliminar rutina?')) {
      eliminarRutina(usuarioId, id)
        .then(() => fetchRutinas())
        .catch((error) => {
          console.error('Error deleting rutina:', error);
        });
    }
  };

  const handleDuplicate = (id) => {
    if (!bebeId || !usuarioId) return;
    duplicarRutina(usuarioId, id)
      .then(() => fetchRutinas())
      .catch((error) => {
        console.error('Error duplicating rutina:', error);
      });
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selectedRutina
      ? actualizarRutina(usuarioId, selectedRutina.id, payload)
      : crearRutina(usuarioId, payload);

    request
      .then(() => {
        setOpenForm(false);
        setSelectedRutina(null);
        fetchRutinas();
      })
      .catch((error) => {
        console.error('Error saving rutina:', error);
      });
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Stack
        direction={{ xs: 'column', sm: 'row' }}
        justifyContent="flex-start"
        alignItems={{ xs: 'stretch', sm: 'center' }}
        spacing={2}
        mb={2}
      >
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          sx={{ alignSelf: 'flex-start', bgcolor: '#20c997', '&:hover': { bgcolor: '#1aa179' } }}
          onClick={handleAdd}
        >
          Añadir rutina
        </Button>
        <FormControl sx={{ minWidth: 150 }}>
          <FormLabel sx={{ mb: 1 }}>Buscar</FormLabel>
          <TextField
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </FormControl>
        <FormControl sx={{ minWidth: 150 }}>
          <FormLabel sx={{ mb: 1 }}>Día</FormLabel>
          <TextField
            select
            value={dayFilter}
            onChange={(e) => setDayFilter(e.target.value)}
          >
            <MenuItem value="">Todos</MenuItem>
            {diasOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        </FormControl>
        <FormControl sx={{ minWidth: 150 }}>
          <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
          <TextField
            select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
          >
            <MenuItem value="">Todos</MenuItem>
            {tipoOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        </FormControl>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Rutinas
      </Typography>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Día de la semana</TableCell>
              <TableCell>Hora</TableCell>
              <TableCell>Tipo de actividad</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredRutinas.length === 0 ? (
              <TableRow>
                <TableCell colSpan={4} align="center">
                  No hay actividades registradas
                </TableCell>
              </TableRow>
            ) : (
              filteredRutinas
                .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                .map((rutina) => (
                  <TableRow key={rutina.id}>
                    <TableCell>{dayMap[rutina.dia] || rutina.dia}</TableCell>
                    <TableCell>{rutina.hora}</TableCell>
                    <TableCell>{rutina.tipo}</TableCell>
                    <TableCell align="center">
                      <IconButton
                        size="small"
                        aria-label="edit"
                        onClick={() => handleEdit(rutina)}
                      >
                        <EditIcon fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        aria-label="delete"
                        onClick={() => handleDelete(rutina.id)}
                        sx={{ color: '#dc3545' }}
                      >
                        <DeleteIcon fontSize="small" />
                      </IconButton>
                      <IconButton
                        size="small"
                        aria-label="duplicate"
                        onClick={() => handleDuplicate(rutina.id)}
                      >
                        <ContentCopyIcon fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))
            )}
          </TableBody>
        </Table>
        <TablePagination
          component="div"
          count={filteredRutinas.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      <RutinaForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedRutina}
      />
    </Box>
  );
}
