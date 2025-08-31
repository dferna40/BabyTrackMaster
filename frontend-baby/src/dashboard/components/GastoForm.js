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

import { listarCategorias } from '../../services/gastosService';

export default function GastoForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    fecha: '',
    categoriaId: '',
    descripcion: '',
    cantidad: '',
  });
  const [categorias, setCategorias] = useState([]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        fecha: initialData.fecha ? new Date(initialData.fecha).toISOString().slice(0, 10) : '',
        categoriaId: initialData.categoriaId || '',
        descripcion: initialData.descripcion || '',
        cantidad: initialData.cantidad || '',
      });
    } else {
      setFormData({ fecha: '', categoriaId: '', descripcion: '', cantidad: '' });
    }
  }, [initialData, open]);

  useEffect(() => {
    listarCategorias()
      .then((response) => {
        setCategorias(response.data);
      })
      .catch((error) => {
        console.error('Error fetching categorias:', error);
      });
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    onSubmit(formData);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{initialData && initialData.id ? 'Editar gasto' : 'Añadir nuevo gasto'}</DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Fecha</FormLabel>
            <TextField
              type="date"
              name="fecha"
              value={formData.fecha}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Categoría</FormLabel>
            <TextField
              select
              name="categoriaId"
              value={formData.categoriaId}
              onChange={handleChange}
            >
              {categorias.map((option) => (
                <MenuItem key={option.id} value={option.id}>
                  {option.nombre}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Descripción</FormLabel>
            <TextField
              multiline
              rows={3}
              name="descripcion"
              value={formData.descripcion}
              onChange={handleChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Cantidad</FormLabel>
            <TextField
              type="number"
              name="cantidad"
              value={formData.cantidad}
              onChange={handleChange}
            />
          </FormControl>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSubmit} variant="contained">Guardar</Button>
      </DialogActions>
    </Dialog>
  );
}
