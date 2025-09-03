import React, { useEffect, useMemo, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
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
import MenuItem from '@mui/material/MenuItem';
import Badge from '@mui/material/Badge';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { LocalizationProvider, DateCalendar, PickersDay } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import {
  listarPorBebe,
  crearCita,
  actualizarCita,
  eliminarCita,
  listarTipos,
  enviarRecordatorio,
} from '../../services/citasService';
import CitaForm from '../components/CitaForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

dayjs.locale('es');

export default function Citas() {
  const [citas, setCitas] = useState([]);
  const [tipos, setTipos] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedCita, setSelectedCita] = useState(null);
  const [search, setSearch] = useState('');
  const [tipoFilter, setTipoFilter] = useState('');
  const [view, setView] = useState('month');
  const [selectedDate, setSelectedDate] = useState(dayjs());

  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const fetchCitas = () => {
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId)
      .then((response) =>
        setCitas(
          response.data.map((c) => ({
            ...c,
            tipoId: c.tipo?.id ?? c.tipoId,
            tipoNombre: c.tipo?.nombre ?? c.tipoNombre,
          }))
        )
      )
      .catch((error) => console.error('Error fetching citas:', error));
  };

  useEffect(() => {
    if (bebeId) {
      fetchCitas();
      listarTipos()
        .then((response) => setTipos(response.data))
        .catch((error) => console.error('Error fetching tipos cita:', error));
    }
  }, [bebeId]);

  const filteredCitas = useMemo(
    () =>
      citas.filter((c) => {
        const matchSearch = search
          ? c.motivo.toLowerCase().includes(search.toLowerCase())
          : true;
        const matchTipo = tipoFilter
          ? String(c.tipoId) === String(tipoFilter)
          : true;
        return matchSearch && matchTipo;
      }),
    [citas, search, tipoFilter]
  );

  const citasSemana = useMemo(
    () => filteredCitas.filter((c) => dayjs(c.fecha).isSame(selectedDate, 'week')),
    [filteredCitas, selectedDate]
  );

  const handleAdd = () => {
    setSelectedCita(null);
    setOpenForm(true);
  };

  const handleEdit = (cita) => {
    setSelectedCita(cita);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId || !usuarioId) return;
    if (window.confirm('¿Eliminar cita?')) {
      eliminarCita(usuarioId, id)
        .then(() => fetchCitas())
        .catch((error) => console.error('Error deleting cita:', error));
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selectedCita
      ? actualizarCita(usuarioId, selectedCita.id, payload)
      : crearCita(usuarioId, payload);

    request
      .then(() => {
        setOpenForm(false);
        setSelectedCita(null);
        fetchCitas();
      })
      .catch((error) => console.error('Error saving cita:', error));
  };

  const handleRecordatorio = (id) => {
    if (!usuarioId) return;
    enviarRecordatorio(usuarioId, id).catch((error) =>
      console.error('Error sending reminder:', error)
    );
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const CustomDay = ({ day, ...props }) => {
    const citasDelDia = citas.filter((c) =>
      dayjs(c.fecha).isSame(day, 'day')
    );
    return (
      <Badge
        key={day.toString()}
        overlap="circular"
        color={citasDelDia.length > 0 ? 'primary' : undefined}
        variant={citasDelDia.length > 0 ? 'dot' : undefined}
      >
        <PickersDay day={day} {...props} />
      </Badge>
    );
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
          Añadir cita
        </Button>
        <TextField
          size="small"
          label="Buscar"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <TextField
          size="small"
          select
          label="Tipo"
          value={tipoFilter}
          onChange={(e) => setTipoFilter(e.target.value)}
          sx={{ minWidth: 150 }}
        >
          <MenuItem value="">Todos</MenuItem>
          {tipos.map((t) => (
            <MenuItem key={t.id} value={t.id}>
              {t.nombre}
            </MenuItem>
          ))}
        </TextField>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Citas
      </Typography>

      <Stack direction="row" spacing={1} mb={2}>
        <Button
          variant={view === 'month' ? 'contained' : 'outlined'}
          onClick={() => setView('month')}
        >
          Mes
        </Button>
        <Button
          variant={view === 'week' ? 'contained' : 'outlined'}
          onClick={() => setView('week')}
        >
          Semana
        </Button>
      </Stack>

      {view === 'month' ? (
        <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="es">
          {selectedDate && (
            <DateCalendar
              value={selectedDate}
              onChange={(newValue) =>
                setSelectedDate(newValue ? dayjs(newValue) : dayjs())
              }
              slots={{ day: CustomDay }}
            />
          )}
        </LocalizationProvider>
      ) : (
        <TableContainer component={Paper} sx={{ mb: 4 }}>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Fecha</TableCell>
                <TableCell>Hora</TableCell>
                <TableCell>Motivo</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell align="center">Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {citasSemana.map((cita) => (
                <TableRow key={cita.id}>
                  <TableCell>
                    {dayjs(cita.fecha).locale('es').format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {dayjs(`${cita.fecha}T${cita.hora}`).locale('es').format('HH:mm')}
                  </TableCell>
                  <TableCell>{cita.motivo}</TableCell>
                  <TableCell>
                    {cita.tipoNombre ||
                      tipos.find((t) => Number(t.id) === Number(cita.tipoId))?.nombre}
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="reminder"
                      onClick={() => handleRecordatorio(cita.id)}
                    >
                      <NotificationsActiveIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(cita)}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(cita.id)}
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Fecha</TableCell>
              <TableCell>Hora</TableCell>
              <TableCell>Motivo</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Centro médico</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredCitas
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((cita) => (
                <TableRow key={cita.id}>
                  <TableCell>
                    {dayjs(cita.fecha).locale('es').format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {dayjs(`${cita.fecha}T${cita.hora}`).locale('es').format('HH:mm')}
                  </TableCell>
                  <TableCell>{cita.motivo}</TableCell>
                  <TableCell>
                    {cita.tipoNombre ||
                      tipos.find((t) => Number(t.id) === Number(cita.tipoId))?.nombre}
                  </TableCell>
                  <TableCell>{cita.centroMedico || '-'}</TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="reminder"
                      onClick={() => handleRecordatorio(cita.id)}
                    >
                      <NotificationsActiveIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(cita)}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(cita.id)}
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
          count={filteredCitas.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>

      <CitaForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedCita}
      />
    </Box>
  );
}

