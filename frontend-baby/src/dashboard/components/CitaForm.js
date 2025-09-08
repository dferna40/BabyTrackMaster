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
import { DatePicker, TimePicker } from '@mui/x-date-pickers';
import { listarTipos, listarEstados } from '../../services/citasService';

export default function CitaForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    fecha: null,
    hora: null,
    motivo: '',
    tipoId: '',
    centroMedico: '',
    estadoId: '',
  });
  const [tipos, setTipos] = useState([]);
  const [estados, setEstados] = useState([]);

  useEffect(() => {
    if (initialData) {
      setFormData({
        fecha: initialData.fecha ? dayjs(initialData.fecha) : null,
        hora: initialData.hora ? dayjs(`1970-01-01T${initialData.hora}`) : null,
        motivo: initialData.motivo || '',
        tipoId: initialData.tipoId || '',
        centroMedico: initialData.centroMedico || '',
        estadoId: initialData.estadoId || '',
      });
    } else {
      setFormData({
        fecha: null,
        hora: null,
        motivo: '',
        tipoId: '',
        centroMedico: '',
        estadoId: '',
      });
    }
  }, [initialData, open]);

  useEffect(() => {
    listarTipos()
      .then((response) => setTipos(response.data))
      .catch((err) => console.error('Error fetching tipos cita:', err));
    if (initialData && initialData.id) {
      listarEstados()
        .then((response) => setEstados(response.data))
        .catch((err) => console.error('Error fetching estados cita:', err));
    }
  }, [initialData]);

  const handleChange = (name, value) => {
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { fecha, hora, motivo, tipoId, centroMedico, estadoId } = formData;
    const data = {
      fecha: fecha ? fecha.format('YYYY-MM-DD') : '',
      hora: hora ? hora.format('HH:mm') : '',
      motivo,
      tipoId,
      centroMedico,
    };
    if (initialData && initialData.id) {
      data.estadoId = estadoId;
    }
    onSubmit(data);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>{initialData && initialData.id ? 'Editar cita' : 'Añadir cita'}</DialogTitle>
        <DialogContent>
          <Stack sx={{ mt: 1 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Fecha</FormLabel>
              <DatePicker
                value={formData.fecha}
                onChange={(newValue) => handleChange('fecha', newValue)}
                slotProps={{ textField: { fullWidth: true } }}
                format="YYYY-MM-DD"
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Hora</FormLabel>
              <TimePicker
                value={formData.hora}
                onChange={(newValue) => handleChange('hora', newValue)}
                slotProps={{ textField: { fullWidth: true } }}
                format="HH:mm"
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Motivo</FormLabel>
              <TextField
                name="motivo"
                value={formData.motivo}
                onChange={(e) => handleChange('motivo', e.target.value)}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
              <TextField
                select
                name="tipoId"
                value={formData.tipoId}
                onChange={(e) => handleChange('tipoId', e.target.value)}
              >
                {tipos.map((t) => (
                  <MenuItem key={t.id} value={t.id}>
                    {t.nombre}
                  </MenuItem>
                ))}
              </TextField>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Centro médico (opcional)</FormLabel>
              <TextField
                name="centroMedico"
                value={formData.centroMedico}
                onChange={(e) => handleChange('centroMedico', e.target.value)}
              />
            </FormControl>
            {initialData && initialData.id && (
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Estado</FormLabel>
                <TextField
                  select
                  name="estadoId"
                  value={formData.estadoId}
                  onChange={(e) => handleChange('estadoId', e.target.value)}
                >
                  {estados.map((e) => (
                    <MenuItem key={e.id} value={e.id}>
                      {e.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button
            variant="contained"
            onClick={onClose}
            sx={{ bgcolor: '#6c757d', '&:hover': { bgcolor: '#5c636a' } }}
          >
            Cancelar
          </Button>
          <Button
            variant="contained"
            onClick={handleSubmit}
            sx={{ bgcolor: '#198754', '&:hover': { bgcolor: '#157347' } }}
          >
            Guardar
          </Button>
        </DialogActions>
      </Dialog>
  );
}

