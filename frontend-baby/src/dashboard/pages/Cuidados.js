import React, { useEffect, useState, useMemo } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import MenuItem from '@mui/material/MenuItem';
import Paper from '@mui/material/Paper';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import Tab from '@mui/material/Tab';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TablePagination from '@mui/material/TablePagination';
import Tabs from '@mui/material/Tabs';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import { BarChart } from '@mui/x-charts/BarChart';
import {
  listarPorBebe,
  crearCuidado,
  actualizarCuidado,
  eliminarCuidado,
} from '../../services/cuidadosService';
import CuidadoForm from '../components/CuidadoForm';

const tipos = ['Biberón', 'Pañal', 'Sueño', 'Baño'];

export default function Cuidados() {
  const [tab, setTab] = useState(0);
  const [cuidados, setCuidados] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedCuidado, setSelectedCuidado] = useState(null);
  const bebeId = 1;
  const [weeklyStats, setWeeklyStats] = useState(Array(7).fill(0));

  const filteredCuidados = useMemo(
  () => cuidados.filter(c => c.tipoNombre === tipos[tab]),
  [cuidados, tab]
);

  useEffect(() => {
  const stats = Array(7).fill(0);
  filteredCuidados.forEach(cuidado => {
    const dayIndex = dayjs(cuidado.inicio).day();
    stats[(dayIndex + 6) % 7] += 1;
  });
  setWeeklyStats(stats);
}, [filteredCuidados]);

  const handleTabChange = (event, newValue) => {
    setTab(newValue);
    setPage(0);
  };

  const fetchCuidados = () => {
    listarPorBebe(bebeId)
      .then((response) => {
        setCuidados(response.data);
      })
      .catch((error) => {
        console.error('Error fetching cuidados:', error);
      });
  };

  useEffect(() => {
    fetchCuidados();
  }, [bebeId]);

  const handleAdd = () => {
    setSelectedCuidado(null);
    setOpenForm(true);
  };

  const handleEdit = (cuidado) => {
    setSelectedCuidado(cuidado);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (window.confirm('¿Eliminar cuidado?')) {
      eliminarCuidado(id)
        .then(() => fetchCuidados())
        .catch((error) => {
          console.error('Error deleting cuidado:', error);
        });
    }
  };

  const handleFormSubmit = (data) => {
    const payload = { ...data, bebeId, usuarioId: 1 };
    const request = selectedCuidado
      ? actualizarCuidado(selectedCuidado.id, payload)
      : crearCuidado(payload);

    request
      .then(() => {
        setOpenForm(false);
        setSelectedCuidado(null);
        fetchCuidados();
      })
      .catch((error) => {
        console.error('Error saving cuidado:', error);
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
        justifyContent="space-between"
        alignItems={{ xs: 'stretch', sm: 'center' }}
        spacing={2}
        mb={2}
      >
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          sx={{ alignSelf: 'flex-start' }}
          onClick={handleAdd}
        >
          Añadir nuevo cuidado
        </Button>
        <Stack direction="row" spacing={2} flexWrap="wrap">
          <TextField
            size="small"
            placeholder="Buscar"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" />
                </InputAdornment>
              ),
            }}
          />
          <Select size="small" defaultValue="Todos">
            <MenuItem value="Todos">Todos</MenuItem>
            <MenuItem value="Biberón">Biberón</MenuItem>
            <MenuItem value="Pañal">Pañal</MenuItem>
            <MenuItem value="Sueño">Sueño</MenuItem>
            <MenuItem value="Baño">Baño</MenuItem>
          </Select>
          <TextField type="date" size="small" />
        </Stack>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Cuidados
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        <Tab label="Biberón" />
        <Tab label="Pañal" />
        <Tab label="Sueño" />
        <Tab label="Baño" />
      </Tabs>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Hora</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Cantidad</TableCell>
              <TableCell>Nota</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredCuidados
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((cuidado) => (
              <TableRow key={cuidado.id}>
                <TableCell>
                  {dayjs(cuidado.inicio).locale('es').format('DD/MM/YYYY HH:mm')}
                </TableCell>
                <TableCell>{cuidado.tipoNombre}</TableCell>
                <TableCell>{cuidado.cantidadMl ?? '-'}</TableCell>
                <TableCell>{cuidado.observaciones}</TableCell>
                <TableCell align="center">
                  <IconButton
                    size="small"
                    aria-label="edit"
                    onClick={() => handleEdit(cuidado)}
                  >
                    <EditIcon fontSize="small" />
                  </IconButton>
                  <IconButton
                    size="small"
                    aria-label="delete"
                    onClick={() => handleDelete(cuidado.id)}
                  >
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </TableCell>
              </TableRow>
              ))}
          </TableBody>
        </Table>
        <TablePagination
          component="div"
          count={filteredCuidados.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      <Card variant="outlined">
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Estadísticas de cuidados
          </Typography>
          <BarChart
            height={250}
            xAxis={[{ scaleType: 'band', data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'] }]}
            series={[{ data: weeklyStats }]}
            margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
            grid={{ horizontal: true }}
          />
        </CardContent>
      </Card>
      <CuidadoForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedCuidado}
      />
    </Box>
  );
}
