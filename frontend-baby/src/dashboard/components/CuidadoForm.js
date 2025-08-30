import React, { useEffect, useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import Stack from '@mui/material/Stack';

const tipoOptions = [
  { id: 1, label: 'Biberón' },
  { id: 2, label: 'Pañal' },
  { id: 3, label: 'Sueño' },
  { id: 4, label: 'Baño' },
];

export default function CuidadoForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    inicio: '',
    tipoId: '',
    cantidadMl: '',
    observaciones: '',
  });

  useEffect(() => {
    if (initialData) {
      setFormData({
        inicio: initialData.inicio ? new Date(initialData.inicio).toISOString().slice(0, 16) : '',
        tipoId: initialData.tipoId || '',
        cantidadMl: initialData.cantidadMl || '',
        observaciones: initialData.observaciones || '',
      });
    } else {
      setFormData({ inicio: '', tipoId: '', cantidadMl: '', observaciones: '' });
    }
  }, [initialData, open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    onSubmit(formData);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{initialData && initialData.id ? 'Editar cuidado' : 'Añadir nuevo cuidado'}</DialogTitle>
      <DialogContent>
        <Stack spacing={2} sx={{ mt: 1 }}>
          <TextField
            label="Inicio"
            type="datetime-local"
            name="inicio"
            value={formData.inicio}
            onChange={handleChange}
            InputLabelProps={{ shrink: true }}
          />
          <TextField
            select
            label="Tipo"
            name="tipoId"
            value={formData.tipoId}
            onChange={handleChange}
          >
            {tipoOptions.map((option) => (
              <MenuItem key={option.id} value={option.id}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            label="Cantidad (ml)"
            type="number"
            name="cantidadMl"
            value={formData.cantidadMl}
            onChange={handleChange}
          />
          <TextField
            label="Observaciones"
            multiline
            rows={3}
            name="observaciones"
            value={formData.observaciones}
            onChange={handleChange}
          />
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSubmit} variant="contained">Guardar</Button>
      </DialogActions>
    </Dialog>
  );
}
