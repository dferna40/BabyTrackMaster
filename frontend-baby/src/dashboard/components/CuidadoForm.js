import React, { useEffect, useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DateTimePicker } from '@mui/x-date-pickers/DateTimePicker';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { listarTipos } from '../../services/cuidadosService';

export default function CuidadoForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    inicio: null,
    tipoId: '',
    cantidadMl: '',
    observaciones: '',
    pecho: '',
  });
  const [tipoOptions, setTipoOptions] = useState([]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        inicio: initialData.inicio ? dayjs(initialData.inicio) : null,
        tipoId: initialData.tipoId || '',
        cantidadMl: initialData.cantidadMl || '',
        observaciones: initialData.observaciones || '',
        pecho: initialData.pecho || '',
      });
    } else {
      setFormData({ inicio: null, tipoId: '', cantidadMl: '', observaciones: '', pecho: '' });
    }
  }, [initialData, open]);

  useEffect(() => {
    listarTipos()
      .then((response) => setTipoOptions(response.data))
      .catch((err) => console.error('Error fetching tipos cuidado:', err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const sanitizedValue =
      name === 'cantidadMl' ? String(Math.max(0, Number(value))) : value;
    setFormData((prev) => ({
      ...prev,
      [name]: sanitizedValue,
      ...(name === 'tipoId' &&
      tipoOptions.find((option) => option.id == sanitizedValue)?.nombre !== 'Pecho'
        ? { pecho: '' }
        : {}),
    }));
  };

  const handleInicioChange = (newValue) => {
    setFormData((prev) => ({ ...prev, inicio: newValue }));
  };

  const isPecho =
    tipoOptions.find((option) => option.id == formData.tipoId)?.nombre === 'Pecho';

  const handleSubmit = () => {
    const payload = {
      ...formData,
      inicio: formData.inicio
        ? formData.inicio.format('YYYY-MM-DDTHH:mm')
        : '',
      pecho: formData.pecho,
    };
    onSubmit(payload);
  };

  return (
    <LocalizationProvider
      dateAdapter={AdapterDayjs}
      adapterLocale="es"
    >
      <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>
          {initialData && initialData.id
            ? 'Editar cuidado'
            : 'Añadir nuevo cuidado'}
        </DialogTitle>
        <DialogContent>
          <Stack sx={{ mt: 1 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Inicio</FormLabel>
              <DateTimePicker
                value={formData.inicio}
                onChange={handleInicioChange}
                slotProps={{ textField: { fullWidth: true } }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
            <TextField
              select
              name="tipoId"
              value={formData.tipoId}
              onChange={handleChange}
            >
              {tipoOptions.map((option) => (
                <MenuItem key={option.id} value={option.id}>
                  {option.nombre}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          {isPecho && (
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Pecho</FormLabel>
              <TextField
                select
                name="pecho"
                value={formData.pecho}
                onChange={handleChange}
              >
                <MenuItem value="Izquierdo">Izquierdo</MenuItem>
                <MenuItem value="Derecho">Derecho</MenuItem>
              </TextField>
            </FormControl>
          )}
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Cantidad</FormLabel>
            <TextField
              type="number"
              name="cantidadMl"
              value={formData.cantidadMl}
              onChange={handleChange}
              inputProps={{ min: 0 }}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Observaciones</FormLabel>
            <TextField
              multiline
              rows={3}
              name="observaciones"
              value={formData.observaciones}
              onChange={handleChange}
            />
          </FormControl>
        </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose}>Cancelar</Button>
          <Button onClick={handleSubmit} variant="contained">
            Guardar
          </Button>
        </DialogActions>
      </Dialog>
    </LocalizationProvider>
  );
}
