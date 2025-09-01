import React, { useEffect, useMemo, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
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
import { BarChart } from '@mui/x-charts/BarChart';
import dayjs from 'dayjs';
import 'dayjs/locale/es';

import {
  listarPorBebe,
  crearGasto,
  actualizarGasto,
  eliminarGasto,
  listarCategorias,
} from '../../services/gastosService';
import GastoForm from '../components/GastoForm';
import { BabyContext } from '../../context/BabyContext';

export default function Gastos() {
  const [gastos, setGastos] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedGasto, setSelectedGasto] = useState(null);
  const [categoryFilter, setCategoryFilter] = useState('');
  const [categorias, setCategorias] = useState([]);
  const [monthFilter, setMonthFilter] = useState(dayjs().format('YYYY-MM'));
  const [weeklyStats, setWeeklyStats] = useState(Array(7).fill(0));
  const { activeBaby } = React.useContext(BabyContext);
  const bebeId = activeBaby?.id;

  const fetchGastos = () => {
    if (!bebeId) return;
    listarPorBebe(bebeId, page, rowsPerPage)
      .then((response) => {
        setGastos(response.data.content ?? response.data);
      })
      .catch((error) => {
        console.error('Error fetching gastos:', error);
      });
  };

  useEffect(() => {
    if (bebeId) {
      fetchGastos();
      listarCategorias()
        .then((response) => {
          setCategorias(response.data);
        })
        .catch((error) => {
          console.error('Error fetching categorias:', error);
        });
    }
  }, [bebeId]);

  const filteredGastos = useMemo(
    () =>
      gastos.filter((g) => {
        const matchCategory = categoryFilter
          ? Number(g.categoriaId) === Number(categoryFilter)
          : true;
        const matchMonth = monthFilter
          ? dayjs(g.fecha).format('YYYY-MM') === monthFilter
          : true;
        return matchCategory && matchMonth;
      }),
    [gastos, categoryFilter, monthFilter]
  );

  const totalMes = useMemo(
    () =>
      filteredGastos.reduce(
        (sum, g) => sum + Number(g.cantidad ?? 0),
        0
      ),
    [filteredGastos]
  );

  useEffect(() => {
    const stats = Array(7).fill(0);
    filteredGastos.forEach((g) => {
      const dayIndex = dayjs(g.fecha).day();
      stats[(dayIndex + 6) % 7] += Number(g.cantidad ?? 0);
    });
    setWeeklyStats(stats);
  }, [filteredGastos]);

  const handleAdd = () => {
    setSelectedGasto(null);
    setOpenForm(true);
  };

  const handleEdit = (gasto) => {
    setSelectedGasto(gasto);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId) return;
    if (window.confirm('¿Eliminar gasto?')) {
      eliminarGasto(id)
        .then(() => fetchGastos())
        .catch((error) => {
          console.error('Error deleting gasto:', error);
        });
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId) return;
    const payload = { ...data, bebeId, usuarioId: 1 };
    const request = selectedGasto
      ? actualizarGasto(selectedGasto.id, payload)
      : crearGasto(payload);

    request
      .then(() => {
        setOpenForm(false);
        setSelectedGasto(null);
        fetchGastos();
      })
      .catch((error) => {
        console.error('Error saving gasto:', error);
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
          sx={{ alignSelf: 'flex-start' }}
          onClick={handleAdd}
        >
          Añadir gasto
        </Button>
        <FormControl sx={{ minWidth: 150 }}>
          <FormLabel sx={{ mb: 1 }}>Categoría</FormLabel>
          <TextField
            select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
          >
            <MenuItem value="">Todas</MenuItem>
            {categorias.map((option) => (
              <MenuItem key={option.id} value={option.id}>
                {option.nombre}
              </MenuItem>
            ))}
          </TextField>
        </FormControl>
        <FormControl sx={{ minWidth: 150 }}>
          <FormLabel sx={{ mb: 1 }}>Mes</FormLabel>
          <TextField
            type="month"
            value={monthFilter}
            onChange={(e) => setMonthFilter(e.target.value)}
          />
        </FormControl>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Gastos
      </Typography>
      <Typography variant="h6" gutterBottom>
        Total del mes: {totalMes.toFixed(2)}
      </Typography>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Fecha</TableCell>
              <TableCell>Categoría</TableCell>
              <TableCell>Descripción</TableCell>
              <TableCell>Cantidad</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredGastos
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((gasto) => (
                <TableRow key={gasto.id}>
                  <TableCell>
                    {dayjs(gasto.fecha).locale('es').format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {gasto.categoriaNombre ||
                      categorias.find(
                        (c) => Number(c.id) === Number(gasto.categoriaId)
                      )?.nombre}
                  </TableCell>
                  <TableCell>{gasto.descripcion}</TableCell>
                  <TableCell>{Number(gasto.cantidad).toFixed(2)}</TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(gasto)}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(gasto.id)}
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
          count={filteredGastos.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      <Card variant="outlined">
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Gastos por día de la semana
          </Typography>
          <BarChart
            height={250}
            xAxis={[
              {
                scaleType: 'band',
                data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
              },
            ]}
            series={[{ data: weeklyStats }]}
            margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
            grid={{ horizontal: true }}
          />
        </CardContent>
      </Card>
      <GastoForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedGasto}
      />
    </Box>
  );
}

