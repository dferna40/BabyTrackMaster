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
import dayjs from 'dayjs';
import { listarTipos } from '../../services/citasService';

export default function CitaForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    fecha: '',
    hora: '',
    motivo: '',
    tipoId: '',
    centroMedico: '',
  });
  const [tipos, setTipos] = useState([]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        fecha: initialData.fecha ? dayjs(initialData.fecha).format('YYYY-MM-DD') : '',
        hora: initialData.fecha ? dayjs(initialData.fecha).format('HH:mm') : '',
        motivo: initialData.motivo || '',
        tipoId: initialData.tipoId || '',
        centroMedico: initialData.centroMedico || '',
      });
    } else {
      setFormData({ fecha: '', hora: '', motivo: '', tipoId: '', centroMedico: '' });
    }
  }, [initialData, open]);

  useEffect(() => {
    listarTipos()
      .then((response) => setTipos(response.data))
      .catch((err) => console.error('Error fetching tipos cita:', err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { fecha, hora, motivo, tipoId, centroMedico } = formData;
    const fechaHora = fecha && hora ? `${fecha}T${hora}` : fecha;
    onSubmit({ fecha: fechaHora, motivo, tipoId, centroMedico });
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>{initialData && initialData.id ? 'Editar cita' : 'Añadir cita'}</DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Fecha</FormLabel>
            <TextField type="date" name="fecha" value={formData.fecha} onChange={handleChange} />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Hora</FormLabel>
            <TextField type="time" name="hora" value={formData.hora} onChange={handleChange} />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Motivo</FormLabel>
            <TextField name="motivo" value={formData.motivo} onChange={handleChange} />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
            <TextField select name="tipoId" value={formData.tipoId} onChange={handleChange}>
              {tipos.map((t) => (
                <MenuItem key={t.id} value={t.id}>
                  {t.nombre}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Centro médico (opcional)</FormLabel>
            <TextField name="centroMedico" value={formData.centroMedico} onChange={handleChange} />
          </FormControl>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button variant="contained" onClick={handleSubmit}>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}

