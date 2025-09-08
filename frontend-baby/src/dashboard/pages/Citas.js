import React, { useEffect, useMemo, useState } from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
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
import Menu from '@mui/material/Menu';
import Chip from '@mui/material/Chip';
import Badge from '@mui/material/Badge';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import NotificationsActiveIcon from '@mui/icons-material/NotificationsActive';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import dayjs from 'dayjs';
import { addButton, cancelButton } from '../../theme/buttonStyles';
import { DateCalendar, PickersDay } from '@mui/x-date-pickers';
import {
  listar,
  crearCita,
  actualizarCita,
  confirmarCita,
  cancelarCita,
  completarCita,
  marcarNoAsistida,
  listarTipos,
  listarEstados,
  enviarRecordatorio,
} from '../../services/citasService';
import CitaForm from '../components/CitaForm';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';

export default function Citas() {
  const [citas, setCitas] = useState([]);
  const [tipos, setTipos] = useState([]);
  const [estados, setEstados] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedCita, setSelectedCita] = useState(null);
  const [search, setSearch] = useState('');
  const [tipoFilter, setTipoFilter] = useState('');
  const [estadoFilter, setEstadoFilter] = useState('');
  const [view, setView] = useState('month');
  const [selectedDate, setSelectedDate] = useState(dayjs());
  const [selectedDay, setSelectedDay] = useState(null);
  const [menuAnchor, setMenuAnchor] = useState(null);
  const [menuCitaId, setMenuCitaId] = useState(null);
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const [openRecordatorioDialog, setOpenRecordatorioDialog] = useState(false);
  const [recordatorioId, setRecordatorioId] = useState(null);
  const [minutosAntelacion, setMinutosAntelacion] = useState(30);

  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  const fetchCitas = () => {
    if (!bebeId || !usuarioId) return;
    listar(bebeId)
      .then((response) => {
        const items = Array.isArray(response.data)
          ? response.data
          : response.data?.content;
        if (Array.isArray(items)) {
          setCitas(
            items.map((c) => ({
              ...c,
              tipoId: c.tipo?.id ?? c.tipoId,
              tipoNombre: c.tipo?.nombre ?? c.tipoNombre,
              estadoId: c.estado?.id ?? c.estadoId,
              estadoNombre: c.estado?.nombre ?? c.estadoNombre,
              tipoEspecialidadId:
                c.tipoEspecialidad?.id ?? c.tipoEspecialidadId,
              tipoEspecialidadNombre:
                c.tipoEspecialidad?.nombre ?? c.tipoEspecialidadNombre,
            }))
          );
        } else {
          console.error('Invalid citas response:', response.data);
          setCitas([]);
          setOpenSnackbar(true);
        }
      })
      .catch((error) => {
        console.error('Error fetching citas:', error);
        setOpenSnackbar(true);
      });
  };

  useEffect(() => {
    if (bebeId) {
      fetchCitas();
      listarTipos()
        .then((response) => setTipos(response.data))
        .catch((error) => console.error('Error fetching tipos cita:', error));
      listarEstados()
        .then((response) => setEstados(response.data))
        .catch((error) => console.error('Error fetching estados cita:', error));
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
        const matchEstado = estadoFilter
          ? String(c.estadoId) === String(estadoFilter)
          : true;
        return matchSearch && matchTipo && matchEstado;
      }),
    [citas, search, tipoFilter, estadoFilter]
  );

  const citasSemana = useMemo(
    () => filteredCitas.filter((c) => dayjs(c.fecha).isSame(selectedDate, 'week')),
    [filteredCitas, selectedDate]
  );

  const citasDelDia = useMemo(
    () =>
      selectedDay
        ? filteredCitas.filter((c) => dayjs(c.fecha).isSame(selectedDay, 'day'))
        : filteredCitas,
    [filteredCitas, selectedDay]
  );

  const getEstadoColor = (estado) => {
    switch (estado?.toLowerCase()) {
      case 'confirmada':
        return 'success';
      case 'completada':
        return 'primary';
      case 'cancelada':
        return 'default';
      case 'no asistida':
        return 'warning';
      default:
        return 'default';
    }
  };

  const handleOpenEstadoMenu = (event, id) => {
    setMenuAnchor(event.currentTarget);
    setMenuCitaId(id);
  };

  const handleCloseEstadoMenu = () => {
    setMenuAnchor(null);
    setMenuCitaId(null);
  };

  const handleCloseSnackbar = (_, reason) => {
    if (reason === 'clickaway') return;
    setOpenSnackbar(false);
  };

  const handleEstadoChange = (accion) => {
    if (!menuCitaId) return;
    const actions = {
      confirmar: confirmarCita,
      cancelar: cancelarCita,
      completar: completarCita,
      noAsistida: marcarNoAsistida,
    };
    actions[accion](menuCitaId)
      .then(() => fetchCitas())
      .catch((error) => console.error('Error changing estado:', error))
      .finally(() => handleCloseEstadoMenu());
  };

  const handleAdd = () => {
    setSelectedCita(null);
    setOpenForm(true);
  };

  const handleEdit = (cita) => {
    setSelectedCita(cita);
    setOpenForm(true);
  };


  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const request = selectedCita
      ? actualizarCita(selectedCita.id, { ...data, bebeId })
      : crearCita({ ...data, bebeId });

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
    setRecordatorioId(id);
    setMinutosAntelacion(30);
    setOpenRecordatorioDialog(true);
  };

  const handleRecordatorioClose = () => {
    setOpenRecordatorioDialog(false);
    setRecordatorioId(null);
  };

  const handleRecordatorioConfirm = () => {
    if (!recordatorioId) return;
    enviarRecordatorio(recordatorioId, minutosAntelacion)
      .catch((error) => console.error('Error sending reminder:', error))
      .finally(() => handleRecordatorioClose());
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
          sx={{ alignSelf: 'flex-start', ...addButton }}
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
        <TextField
          size="small"
          select
          label="Estado"
          value={estadoFilter}
          onChange={(e) => setEstadoFilter(e.target.value)}
          sx={{ minWidth: 150 }}
        >
          <MenuItem value="">Todos</MenuItem>
          {estados.map((e) => (
            <MenuItem key={e.id} value={e.id}>
              {e.nombre}
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
        <>
          {selectedDate && (
            <DateCalendar
              value={selectedDate}
              onChange={(newValue) => {
                const date = newValue ? dayjs(newValue) : dayjs();
                setSelectedDate(date);
                setSelectedDay(date);
              }}
              slots={{ day: CustomDay }}
            />
          )}
          {selectedDay && (
            <Button
              variant="outlined"
              onClick={() => setSelectedDay(null)}
              sx={{ mb: 2 }}
            >
              Ver todas las citas
            </Button>
          )}
        </>
      ) : (
        <TableContainer sx={{ mb: 4 }}>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Fecha</TableCell>
                <TableCell>Hora</TableCell>
                <TableCell>Motivo</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell>Estado</TableCell>
                <TableCell align="center">Acciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {citasSemana.map((cita) => (
                <TableRow key={cita.id}>
                  <TableCell>
                    {dayjs(cita.fecha).format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {dayjs(`${cita.fecha}T${cita.hora}`).format('HH:mm')}
                  </TableCell>
                  <TableCell>{cita.motivo}</TableCell>
                  <TableCell>
                    {cita.tipoNombre ||
                      tipos.find((t) => Number(t.id) === Number(cita.tipoId))?.nombre}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={cita.estadoNombre}
                      color={getEstadoColor(cita.estadoNombre)}
                      size="small"
                    />
                  </TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="reminder"
                      onClick={() => handleRecordatorio(cita.id)}
                      sx={{ color: '#6c757d' }}
                    >
                      <NotificationsActiveIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(cita)}
                      sx={{ color: '#0d6efd' }}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="estado"
                      onClick={(e) => handleOpenEstadoMenu(e, cita.id)}
                      sx={{ color: '#6c757d' }}
                    >
                      <MoreVertIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}

      <TableContainer sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Fecha</TableCell>
              <TableCell>Hora</TableCell>
              <TableCell>Motivo</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Estado</TableCell>
              <TableCell>Centro médico</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {citasDelDia
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((cita) => (
                <TableRow key={cita.id}>
                  <TableCell>
                    {dayjs(cita.fecha).format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {dayjs(`${cita.fecha}T${cita.hora}`).format('HH:mm')}
                  </TableCell>
                  <TableCell>{cita.motivo}</TableCell>
                  <TableCell>
                    {cita.tipoNombre ||
                      tipos.find((t) => Number(t.id) === Number(cita.tipoId))?.nombre}
                  </TableCell>
                  <TableCell>
                    <Chip
                      label={cita.estadoNombre}
                      color={getEstadoColor(cita.estadoNombre)}
                      size="small"
                    />
                  </TableCell>
                  <TableCell>{cita.centroMedico || '-'}</TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="reminder"
                      onClick={() => handleRecordatorio(cita.id)}
                      sx={{ color: '#6c757d' }}
                    >
                      <NotificationsActiveIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(cita)}
                      sx={{ color: '#0d6efd' }}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="estado"
                      onClick={(e) => handleOpenEstadoMenu(e, cita.id)}
                      sx={{ color: '#6c757d' }}
                    >
                      <MoreVertIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      <TablePagination
          component="div"
          count={citasDelDia.length}
          page={page}
          onPageChange={handleChangePage}
          rowsPerPage={rowsPerPage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </TableContainer>
      <Menu
        anchorEl={menuAnchor}
        open={Boolean(menuAnchor)}
        onClose={handleCloseEstadoMenu}
      >
        <MenuItem onClick={() => handleEstadoChange('confirmar')}>
          Confirmar
        </MenuItem>
        <MenuItem onClick={() => handleEstadoChange('completar')}>
          Completar
        </MenuItem>
        <MenuItem onClick={() => handleEstadoChange('noAsistida')}>
          No asistida
        </MenuItem>
        <MenuItem onClick={() => handleEstadoChange('cancelar')}>
          Cancelar
        </MenuItem>
      </Menu>

      <Dialog open={openRecordatorioDialog} onClose={handleRecordatorioClose}>
        <DialogTitle>Enviar recordatorio</DialogTitle>
        <DialogContent>
          <TextField
            type="number"
            label="Minutos de antelación"
            value={minutosAntelacion}
            onChange={(e) =>
              setMinutosAntelacion(Math.max(0, Number(e.target.value)))
            }
            inputProps={{ min: 0 }}
            fullWidth
            sx={{ mt: 1 }}
          />
        </DialogContent>
        <DialogActions>
          <Button
            variant="contained"
            onClick={handleRecordatorioClose}
            sx={cancelButton}
          >
            Cancelar
          </Button>
          <Button onClick={handleRecordatorioConfirm} variant="contained">
            Enviar
          </Button>
        </DialogActions>
      </Dialog>

      <CitaForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedCita}
      />
      <Snackbar
        open={openSnackbar}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity="error"
          sx={{ width: '100%' }}
        >
          Error al cargar citas
        </Alert>
      </Snackbar>
    </Box>
  );
}

