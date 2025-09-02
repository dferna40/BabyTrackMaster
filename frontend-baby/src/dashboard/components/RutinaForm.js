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

const diasOptions = [
  { value: 'L', label: 'Lunes' },
  { value: 'M', label: 'Martes' },
  { value: 'X', label: 'Miércoles' },
  { value: 'J', label: 'Jueves' },
  { value: 'V', label: 'Viernes' },
  { value: 'S', label: 'Sábado' },
  { value: 'D', label: 'Domingo' },
];

const tipoOptions = [
  { value: 'Alimentación', label: 'Alimentación' },
  { value: 'Juego', label: 'Juego' },
  { value: 'Sueño', label: 'Sueño' },
  { value: 'Baño', label: 'Baño' },
];

export default function RutinaForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    dia: '',
    hora: '',
    tipo: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        dia: initialData.dia || '',
        hora: initialData.hora || '',
        tipo: initialData.tipo || '',
      });
    } else {
      setFormData({ dia: '', hora: '', tipo: '' });
    }
  }, [initialData, open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    if (!formData.dia || !formData.hora || !formData.tipo) return;
    onSubmit(formData);
  };

  const isValid = formData.dia && formData.hora && formData.tipo;

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{initialData && initialData.id ? 'Editar rutina' : 'Añadir rutina'}</DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Día de la semana</FormLabel>
            <TextField
              select
              name="dia"
              value={formData.dia}
              onChange={handleChange}
            >
              {diasOptions.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Hora</FormLabel>
            <TextField
              type="time"
              name="hora"
              value={formData.hora}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Tipo de actividad</FormLabel>
            <TextField
              select
              name="tipo"
              value={formData.tipo}
              onChange={handleChange}
            >
              {tipoOptions.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSubmit} variant="contained" disabled={!isValid}>
          Guardar rutina
        </Button>
      </DialogActions>
    </Dialog>
  );
}
