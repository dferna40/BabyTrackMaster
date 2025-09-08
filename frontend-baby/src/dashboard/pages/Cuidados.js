import React, { useEffect, useState, useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
import Paper from '@mui/material/Paper';
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
import Typography from '@mui/material/Typography';
import dayjs from 'dayjs';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { BarChart } from '@mui/x-charts/BarChart';
import {
  listarPorBebe,
  crearCuidado,
  actualizarCuidado,
  eliminarCuidado,
  listarTipos,
} from '../../services/cuidadosService';
import CuidadoForm from '../components/CuidadoForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

export default function Cuidados() {
  const [tab, setTab] = useState(0);
  const [cuidados, setCuidados] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedCuidado, setSelectedCuidado] = useState(null);
  const [tipos, setTipos] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;
  const [weeklyStats, setWeeklyStats] = useState(Array(7).fill(0));
  const location = useLocation();

  const filteredCuidados = useMemo(
    () =>
      tipos[tab]
        ? cuidados.filter(
            (c) => Number(c.tipoId) === Number(tipos[tab].id)
          )
        : [],
    [cuidados, tab, tipos]
  );

  const esPecho = tipos[tab]?.nombre === 'Pecho';

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
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId)
      .then((response) => {
        setCuidados(response.data);
      })
      .catch((error) => {
        console.error('Error fetching cuidados:', error);
      });
  };

  useEffect(() => {
    if (bebeId) {
      fetchCuidados();
      listarTipos()
        .then((response) => setTipos(response.data))
        .catch((error) => console.error('Error fetching tipos cuidado:', error));
    }
  }, [bebeId]);

  useEffect(() => {
    if (location.state?.tipo && tipos.length > 0) {
      const tipo = tipos.find(
        (t) => t.nombre.toLowerCase() === location.state.tipo.toLowerCase()
      );
      if (tipo) {
        setSelectedCuidado({ tipoId: tipo.id });
        setOpenForm(true);
      }
    }
  }, [location.state, tipos]);

  const handleAdd = () => {
    setSelectedCuidado(null);
    setOpenForm(true);
  };

  const handleEdit = (cuidado) => {
    setSelectedCuidado(cuidado);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId || !usuarioId) return;
    if (window.confirm('¿Eliminar cuidado?')) {
      eliminarCuidado(usuarioId, id)
        .then(() => fetchCuidados())
        .catch((error) => {
          console.error('Error deleting cuidado:', error);
        });
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selectedCuidado
      ? actualizarCuidado(usuarioId, selectedCuidado.id, payload)
      : crearCuidado(usuarioId, payload);

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

  const handleExportCsv = () => {
    const headers = ['Hora', 'Tipo', esPecho ? 'Pecho' : 'Cantidad', 'Nota'];
    const rows = filteredCuidados.map((cuidado) => [
      dayjs(cuidado.inicio).format('DD/MM/YYYY HH:mm'),
      cuidado.tipoNombre,
      esPecho ? cuidado.pecho ?? '-' : cuidado.cantidadMl ?? '-',
      cuidado.observaciones ?? '',
    ]);
    const csvContent = [headers, ...rows].map((e) => e.join(',')).join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute(
      'download',
      `cuidados_${tipos[tab]?.nombre.toLowerCase()}.csv`
    );
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  const handleExportPdf = () => {
    const doc = new jsPDF();
    const tableColumn = ['Hora', 'Tipo', esPecho ? 'Pecho' : 'Cantidad', 'Nota'];
    const tableRows = filteredCuidados.map((cuidado) => [
      dayjs(cuidado.inicio).format('DD/MM/YYYY HH:mm'),
      cuidado.tipoNombre,
      esPecho ? cuidado.pecho ?? '-' : cuidado.cantidadMl ?? '-',
      cuidado.observaciones ?? '',
    ]);
    autoTable(doc, {
      head: [tableColumn],
      body: tableRows,
    });
    doc.save(`cuidados_${tipos[tab]?.nombre.toLowerCase()}.pdf`);
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
          Añadir nuevo cuidado
        </Button>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Cuidados
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        {tipos.map((t) => (
          <Tab key={t.id} label={t.nombre} />
        ))}
      </Tabs>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Hora</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>{esPecho ? 'Pecho' : 'Cantidad'}</TableCell>
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
                  {dayjs(cuidado.inicio).format('DD/MM/YYYY HH:mm')}
                </TableCell>
                <TableCell>{cuidado.tipoNombre}</TableCell>
                <TableCell>{esPecho ? cuidado.pecho ?? '-' : cuidado.cantidadMl ?? '-'}</TableCell>
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
          count={filteredCuidados.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      {filteredCuidados.length > 0 && (
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
