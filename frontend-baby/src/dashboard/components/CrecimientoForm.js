import React, { useState, useEffect } from 'react';
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
import { DatePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import { listarTipos } from '../../services/crecimientoService';
import { saveButton, cancelButton } from '../../theme/buttonStyles';

export default function CrecimientoForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    fecha: null,
    tipoId: '',
    valor: '',
    unidad: '',
    observaciones: '',
  });
  const [tipoOptions, setTipoOptions] = useState([]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        fecha: initialData.fecha ? dayjs(initialData.fecha) : null,
        tipoId: initialData.tipoId || '',
        valor: initialData.valor || '',
        unidad: initialData.unidad || '',
        observaciones: initialData.observaciones || '',
      });
    } else {
      setFormData({
        fecha: null,
        tipoId: '',
        valor: '',
        unidad: '',
        observaciones: '',
      });
    }
  }, [initialData, open]);

  useEffect(() => {
    listarTipos()
      .then((res) => setTipoOptions(res.data))
      .catch((err) => console.error('Error fetching tipos crecimiento:', err));
  }, []);

  const getUnidad = (tipoId) => {
    const tipo = tipoOptions.find((t) => t.id == tipoId);
    if (!tipo) return '';
    const nombre = tipo.nombre.toLowerCase();
    if (nombre.includes('peso')) return 'kg';
    if (nombre.includes('talla') || nombre.includes('perímetro') || nombre.includes('perimetro')) return 'cm';
    return '';
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
      ...(name === 'tipoId' ? { unidad: getUnidad(value) } : {}),
    }));
  };

  const handleFechaChange = (newValue) => {
    setFormData((prev) => ({ ...prev, fecha: newValue }));
  };

  const handleSubmit = () => {
    if (!formData.fecha) return;
    const payload = {
      ...formData,
      fecha: formData.fecha ? formData.fecha.format('YYYY-MM-DD') : '',
      valor: formData.valor ? parseFloat(formData.valor) : null,
    };
    onSubmit(payload);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {initialData && initialData.id ? 'Editar registro' : 'Añadir registro'}
      </DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Fecha</FormLabel>
            <DatePicker
              value={formData.fecha}
              onChange={handleFechaChange}
              slotProps={{ textField: { fullWidth: true, required: true } }}
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
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Valor</FormLabel>
            <TextField
              type="number"
              name="valor"
              value={formData.valor}
              onChange={handleChange}
              inputProps={{ min: 0, step: 'any' }}
            />
          </FormControl>
          {formData.unidad && (
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Unidad</FormLabel>
              <TextField
                name="unidad"
                value={formData.unidad}
                onChange={handleChange}
              />
            </FormControl>
          )}
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
        <Button variant="contained" onClick={onClose} sx={cancelButton}>
          Cancelar
        </Button>
        <Button variant="contained" onClick={handleSubmit} sx={saveButton}>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}
