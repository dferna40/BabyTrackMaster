import React, { useEffect, useState, useMemo } from 'react';
import { useLocation } from 'react-router-dom';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
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
import { useTheme } from '@mui/material/styles';

import {
  listarPorBebe,
  obtenerEstadisticas,
  crearRegistro,
  actualizarRegistro,
  eliminarRegistro,
  listarTiposLactancia,
  listarTiposAlimentacion,
} from '../../services/alimentacionService';
import { addButton } from '../../theme/buttonStyles';
import AlimentacionForm from '../components/AlimentacionForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

export default function Alimentacion() {
  const [tiposAlimentacion, setTiposAlimentacion] = useState([]);
  const [tab, setTab] = useState(null);
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
  const location = useLocation();
  const theme = useTheme();

  const normalize = (str) =>
    str?.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');

  const selectedTipo = useMemo(
    () => tiposAlimentacion.find((t) => t.id === tab),
    [tiposAlimentacion, tab]
  );
  const selectedSlug = normalize(selectedTipo?.nombre);

  const filtered = useMemo(
    () => registros.filter((r) => r.tipoAlimentacion?.id === tab),
    [registros, tab]
  );

  const lactanciaId = useMemo(
    () =>
      tiposAlimentacion.find((t) => normalize(t.nombre) === 'lactancia')?.id,
    [tiposAlimentacion]
  );
  const biberonId = useMemo(
    () =>
      tiposAlimentacion.find((t) => normalize(t.nombre) === 'biberon')?.id,
    [tiposAlimentacion]
  );
  const lactanciaCount = useMemo(
    () =>
      (lactanciaId
        ? registros.filter((r) => r.tipoAlimentacion?.id === lactanciaId).length
        : 0),
    [registros, lactanciaId]
  );
  const biberonCount = useMemo(
    () =>
      (biberonId
        ? registros.filter((r) => r.tipoAlimentacion?.id === biberonId).length
        : 0),
    [registros, biberonId]
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
      .then((res) => {
        const weekly = res.data?.weekly ?? [];
        setWeeklyStats(Array.from({ length: 7 }, (_, i) => weekly[i] ?? 0));
      })
      .catch((err) => console.error('Error fetching estadisticas:', err));
  };

  useEffect(() => {
    if (bebeId) {
      fetchRegistros();
      fetchEstadisticas();
    }
  }, [bebeId]);

  useEffect(() => {
    listarTiposAlimentacion()
      .then((res) => {
        setTiposAlimentacion(res.data);
        setTab(res.data[0]?.id ?? null);
      })
      .catch((err) =>
        console.error('Error fetching tipos alimentacion:', err)
      );
  }, []);

  useEffect(() => {
    const state = location.state;
    if (!state?.tipo || tiposAlimentacion.length === 0) return;

    const { tipo, tipoLactancia, disableTipo, disableTipoLactancia } = state;
    const found = tiposAlimentacion.find(
      (t) => normalize(t.nombre) === tipo.toLowerCase()
    );
    if (found) {
      setTab(found.id);
    }

    const openWithSelected = (tipoLactanciaData) => {
      setSelected({
        tipoAlimentacion: found ? { id: found.id } : undefined,
        tipoLactancia: tipoLactanciaData,
        disableTipo,
        disableTipoLactancia,
      });
      setOpenForm(true);
    };

    if (tipoLactancia && typeof tipoLactancia === 'string') {
      listarTiposLactancia()
        .then((res) => {
          const tl = res.data?.find(
            (t) => t.nombre?.toLowerCase() === tipoLactancia.toLowerCase()
          );
          if (tl) {
            openWithSelected(tl);
          } else {
            openWithSelected({ nombre: tipoLactancia });
          }
        })
        .catch((err) => {
          console.error('Error fetching tipos lactancia:', err);
          openWithSelected({ nombre: tipoLactancia });
        });
    } else {
      openWithSelected(tipoLactancia);
    }
  }, [location.state, tiposAlimentacion]);

  const handleAdd = () => {
    setSelected({ tipoAlimentacion: { id: tab } });
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
    lactancia: [
      'Hora',
      'Tipo lactancia',
      'Lado',
      'Sólido (g)',
      'Cantidad leche fórmula (ml)',
      'Cantidad otros alimentos (ml)',
      'Duración (min)',
      'Observaciones',
    ],
    biberon: ['Hora', 'Tipo', 'Cantidad (ml)', 'Observaciones'],
    solidos: ['Hora', 'Alimento', 'Cantidad (g)', 'Observaciones'],
  };

  const handleExportCsv = () => {
    const current = selectedSlug;
    const headers = headersMap[current];
    const rows = filtered.map((r) => {
      const base = [dayjs(r.inicio).format('DD/MM/YYYY HH:mm')];
      if (current === 'lactancia') {
        return [
          ...base,
          r.tipoLactancia?.nombre || '',
          r.lado || '',
          r.alimento || '',
          r.cantidadLecheFormula || '',
          r.cantidadOtrosAlimentos || '',
          r.duracionMin || '',
          r.observaciones || '',
        ];
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
    const current = selectedSlug;
    const headers = headersMap[current];
    const rows = filtered.map((r) => {
      const base = [dayjs(r.inicio).format('DD/MM/YYYY HH:mm')];
      if (current === 'lactancia') {
        return [
          ...base,
          r.tipoLactancia?.nombre || '',
          r.lado || '',
          r.alimento || '',
          r.cantidadLecheFormula || '',
          r.cantidadOtrosAlimentos || '',
          r.duracionMin || '',
          r.observaciones || '',
        ];
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
          sx={{ alignSelf: 'flex-start', ...addButton }}
          onClick={handleAdd}
        >
          Registrar alimentación
        </Button>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Alimentación
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        {tiposAlimentacion.map((t) => (
          <Tab key={t.id} label={t.nombre} value={t.id} />
        ))}
      </Tabs>

      <TableContainer sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              {headersMap[selectedSlug]?.map((h) => (
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
                    {dayjs(r.inicio).format('DD/MM/YYYY HH:mm')}
                  </TableCell>
                  {selectedSlug === 'lactancia' && (
                    <>
                      <TableCell>{r.tipoLactancia?.nombre}</TableCell>
                      <TableCell>{r.lado}</TableCell>
                      <TableCell>{r.alimento}</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>
                        {r.cantidadLecheFormula}
                      </TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>
                        {r.cantidadOtrosAlimentos}
                      </TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>{r.duracionMin}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  {selectedSlug === 'biberon' && (
                    <>
                      <TableCell>{r.tipoLeche}</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>{r.cantidadMl}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  {selectedSlug === 'solidos' && (
                    <>
                      <TableCell>{r.alimento}</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>{r.cantidadMl}</TableCell>
                      <TableCell>{r.observaciones}</TableCell>
                    </>
                  )}
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(r)}
                      sx={{ color: '#0d6efd' }}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(r.id)}
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
              colors={Object.values(theme.palette.chart)}
              borderRadius={8}
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
              series={[
                { data: [lactanciaCount, 0], color: theme.palette.chart.babyBlue },
                { data: [0, biberonCount], color: theme.palette.chart.babyPink },
              ]}
              margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
              grid={{ horizontal: true }}
              colors={Object.values(theme.palette.chart)}
              borderRadius={8}
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

