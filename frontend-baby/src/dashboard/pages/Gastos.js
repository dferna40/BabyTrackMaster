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
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

import {
  listarPorBebe,
  crearGasto,
  actualizarGasto,
  eliminarGasto,
  listarCategorias,
} from '../../services/gastosService';
import { addButton } from '../../theme/buttonStyles';
import GastoForm from '../components/GastoForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

export default function Gastos() {
  const [gastos, setGastos] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedGasto, setSelectedGasto] = useState(null);
  const [categoryFilter, setCategoryFilter] = useState('0');
  const [categorias, setCategorias] = useState([]);
  const [monthFilter, setMonthFilter] = useState(dayjs().format('YYYY-MM'));
  const [weeklyStats, setWeeklyStats] = useState(Array(7).fill(0));
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const fetchGastos = () => {
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId, page, rowsPerPage)
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
        const matchCategory =
          categoryFilter !== '0'
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
    if (!bebeId || !usuarioId) return;
    if (window.confirm('¿Eliminar gasto?')) {
      eliminarGasto(usuarioId, id)
        .then(() => fetchGastos())
        .catch((error) => {
          console.error('Error deleting gasto:', error);
        });
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selectedGasto
      ? actualizarGasto(usuarioId, selectedGasto.id, payload)
      : crearGasto(usuarioId, payload);

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

  const handleExportCsv = () => {
    const headers = ['Fecha', 'Categoría', 'Descripción', 'Cantidad'];
    const rows = filteredGastos.map((gasto) => [
      dayjs(gasto.fecha).format('DD/MM/YYYY'),
      gasto.categoriaNombre ||
        categorias.find((c) => Number(c.id) === Number(gasto.categoriaId))?.nombre ||
        '',
      gasto.descripcion || '',
      Number(gasto.cantidad).toFixed(2),
    ]);
    const csvContent = [headers, ...rows].map((e) => e.join(',')).join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    const categoriaNombre =
      categoryFilter !== '0'
        ? categorias.find((c) => Number(c.id) === Number(categoryFilter))?.nombre || 'general'
        : 'general';
    link.setAttribute('download', `gastos_${categoriaNombre.toLowerCase()}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleExportPdf = () => {
    const doc = new jsPDF();
    const tableColumn = ['Fecha', 'Categoría', 'Descripción', 'Cantidad'];
    const tableRows = filteredGastos.map((gasto) => [
      dayjs(gasto.fecha).format('DD/MM/YYYY'),
      gasto.categoriaNombre ||
        categorias.find((c) => Number(c.id) === Number(gasto.categoriaId))?.nombre ||
        '',
      gasto.descripcion || '',
      Number(gasto.cantidad).toFixed(2),
    ]);
    autoTable(doc, {
      head: [tableColumn],
      body: tableRows,
    });
    const categoriaNombre =
      categoryFilter !== '0'
        ? categorias.find((c) => Number(c.id) === Number(categoryFilter))?.nombre || 'general'
        : 'general';
    doc.save(`gastos_${categoriaNombre.toLowerCase()}.pdf`);
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
          sx={{ alignSelf: 'flex-start', ...addButton }}
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
            <MenuItem value="0">Todas</MenuItem>
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
                    {dayjs(gasto.fecha).format('DD/MM/YYYY')}
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
          sx={{ color: '#dc3545' }}
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
      {filteredGastos.length > 0 && (
        <Box sx={{ mb: 4, display: 'flex', gap: 2 }}>
          <Button variant="outlined" onClick={handleExportPdf}>
            Exportar PDF
          </Button>
          <Button variant="outlined" onClick={handleExportCsv}>
            Exportar CSV
          </Button>
        </Box>
      )}

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

