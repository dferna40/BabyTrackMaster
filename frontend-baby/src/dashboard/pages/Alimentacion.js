import React, { useEffect, useState, useMemo } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import Tab from '@mui/material/Tab';
import Tabs from '@mui/material/Tabs';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import dayjs from 'dayjs';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { BarChart } from '@mui/x-charts/BarChart';

import {
  listarPorBebe,
  obtenerEstadisticas,
  crearRegistro,
  actualizarRegistro,
  eliminarRegistro,
} from '../../services/alimentacionService';
import AlimentacionForm from '../components/AlimentacionForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

export default function Alimentacion() {
  const tabLabels = ['Lactancia', 'Biberón', 'Sólidos'];
  const tabValues = ['lactancia', 'biberon', 'solidos'];
  const [tab, setTab] = useState(0);
  const [registros, setRegistros] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selected, setSelected] = useState(null);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const bebeId = activeBaby?.id;
  const usuarioId = user?.id;
  const [weeklyStats, setWeeklyStats] = useState(Array(7).fill(0));

  const filtered = useMemo(
    () => registros.filter((r) => r.tipo === tabValues[tab]),
    [registros, tab]
  );

  const lactanciaCount = useMemo(
    () => registros.filter((r) => r.tipo === 'lactancia').length,
    [registros]
  );
  const biberonCount = useMemo(
    () => registros.filter((r) => r.tipo === 'biberon').length,
    [registros]
  );

  const handleTabChange = (event, newValue) => {
    setTab(newValue);
    setPage(0);
  };

  const fetchRegistros = () => {
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId)
      .then((res) => setRegistros(res.data))
      .catch((err) => console.error('Error fetching alimentacion:', err));
  };

  const fetchEstadisticas = () => {
    if (!bebeId || !usuarioId) return;
    obtenerEstadisticas(usuarioId, bebeId)
      .then((res) => setWeeklyStats(res.data?.weekly || res.data || Array(7).fill(0)))
      .catch((err) => console.error('Error fetching estadisticas:', err));
  };

  useEffect(() => {
    if (bebeId) {
      fetchRegistros();
      fetchEstadisticas();
    }
  }, [bebeId]);

  const handleAdd = () => {
    setSelected({ tipo: tabValues[tab] });
    setOpenForm(true);
  };

  const handleEdit = (reg) => {
    setSelected(reg);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId || !usuarioId) return;
    if (window.confirm('¿Eliminar registro?')) {
      eliminarRegistro(usuarioId, bebeId, id)
        .then(() => {
          fetchRegistros();
          fetchEstadisticas();
        })
        .catch((err) => console.error('Error deleting registro:', err));
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selected && selected.id
      ? actualizarRegistro(usuarioId, bebeId, selected.id, payload)
      : crearRegistro(usuarioId, bebeId, payload);
    request
      .then(() => {
        setOpenForm(false);
        setSelected(null);
        fetchRegistros();
        fetchEstadisticas();
      })
      .catch((err) => console.error('Error saving registro:', err));
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const headersMap = {
    lactancia: ['Hora', 'Lado', 'Duración', 'Observaciones'],
    biberon: ['Hora', 'Tipo', 'Cantidad (ml)', 'Observaciones'],
    solidos: ['Hora', 'Alimento', 'Cantidad', 'Observaciones'],
  };

  const handleExportCsv = () => {
    const current = tabValues[tab];
    const headers = headersMap[current];
    const rows = filtered.map((r) => {
      const base = [dayjs(r.fechaHora).format('DD/MM/YYYY HH:mm')];
      if (current === 'lactancia') {
        return [...base, r.lado, r.duracionMin, r.observaciones || ''];
      }
      if (current === 'biberon') {
        return [...base, r.tipoLeche, r.cantidadMl, r.observaciones || ''];
      }
      return [...base, r.alimento, r.cantidadMl, r.observaciones || ''];
    });
    const csvContent = [headers, ...rows].map((e) => e.join(',')).join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `alimentacion_${current}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleExportPdf = () => {
    const current = tabValues[tab];
    const headers = headersMap[current];
    const rows = filtered.map((r) => {
      const base = [dayjs(r.fechaHora).format('DD/MM/YYYY HH:mm')];
      if (current === 'lactancia') {
        return [...base, r.lado, r.duracionMin, r.observaciones || ''];
      }
      if (current === 'biberon') {
        return [...base, r.tipoLeche, r.cantidadMl, r.observaciones || ''];
      }
      return [...base, r.alimento, r.cantidadMl, r.observaciones || ''];
    });
    const doc = new jsPDF();
    autoTable(doc, {
      head: [headers],
      body: rows,
    });
    doc.save(`alimentacion_${current}.pdf`);
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
          Añadir registro
        </Button>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Alimentación
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        {tabLabels.map((label) => (
          <Tab key={label} label={label} />
        ))}
      </Tabs>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              {headersMap[tabValues[tab]].map((h) => (
                <TableCell key={h}>{h}</TableCell>
              ))}
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filtered
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((r) => (
                <TableRow key={r.id}>
                  <TableCell>
                    {dayjs(r.fechaHora).format('DD/MM/YYYY HH:mm')}
                  </TableCell>
                  {tabValues[tab] === 'lactancia' && (
                    <>
                      <TableCell>{r.lado}</TableCell>
                      <TableCell>{r.duracionMin}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  {tabValues[tab] === 'biberon' && (
                    <>
                      <TableCell>{r.tipoLeche}</TableCell>
                      <TableCell>{r.cantidadMl}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  {tabValues[tab] === 'solidos' && (
                    <>
                      <TableCell>{r.alimento}</TableCell>
                      <TableCell>{r.cantidadMl}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(r)}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(r.id)}
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
          count={filtered.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      {filtered.length > 0 && (
        <Box sx={{ mb: 4, display: 'flex', gap: 2 }}>
          <Button variant="outlined" onClick={handleExportPdf}>
            Exportar PDF
          </Button>
          <Button variant="outlined" onClick={handleExportCsv}>
            Exportar CSV
          </Button>
        </Box>
      )}

      <Stack spacing={2}>
        <Card variant="outlined">
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Totales por día de la semana
            </Typography>
            <BarChart
              height={250}
              xAxis={[{
                scaleType: 'band',
                data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'],
              }]}
              series={[{ data: weeklyStats }]}
              margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
              grid={{ horizontal: true }}
            />
          </CardContent>
        </Card>
        <Card variant="outlined">
          <CardContent>
            <Typography variant="h6" gutterBottom>
              Comparativa lactancia vs biberón
            </Typography>
            <BarChart
              height={250}
              xAxis={[{ scaleType: 'band', data: ['Lactancia', 'Biberón'] }]}
              series={[{ data: [lactanciaCount, biberonCount] }]}
              margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
              grid={{ horizontal: true }}
            />
          </CardContent>
        </Card>
      </Stack>

      <AlimentacionForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selected}
      />
    </Box>
  );
}

