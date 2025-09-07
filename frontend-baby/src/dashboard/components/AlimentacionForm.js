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
import { DateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';

const tipos = [
  { value: 'lactancia', label: 'Lactancia' },
  { value: 'biberon', label: 'Biberón' },
  { value: 'solidos', label: 'Sólidos' },
];

export default function AlimentacionForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    tipo: 'lactancia',
    inicio: null,
    lado: '',
    duracionMin: '',
    tipoLeche: '',
    cantidadMl: '',
    alimento: '',
    observaciones: '',
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setFormData({
        tipo: initialData.tipo || 'lactancia',
        inicio: initialData.fechaHora ? dayjs(initialData.fechaHora) : null,
        lado: initialData.lado || '',
        duracionMin: initialData.duracionMin || '',
        tipoLeche: initialData.tipoLeche || '',
        cantidadMl: initialData.cantidadMl || '',
        alimento: initialData.alimento || '',
        observaciones: initialData.observaciones || '',
      });
    } else {
      setFormData({
        tipo: initialData?.tipo || 'lactancia',
        inicio: null,
        lado: '',
        duracionMin: '',
        tipoLeche: '',
        cantidadMl: '',
        alimento: '',
        observaciones: '',
      });
    }
    setErrors({});
  }, [initialData, open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleInicioChange = (newValue) => {
    setFormData((prev) => ({ ...prev, inicio: newValue }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.inicio) newErrors.inicio = 'Requerido';
    if (formData.tipo === 'lactancia') {
      if (!formData.lado) newErrors.lado = 'Requerido';
      if (!formData.duracionMin) newErrors.duracionMin = 'Requerido';
    }
    if (formData.tipo === 'biberon') {
      if (!formData.tipoLeche) newErrors.tipoLeche = 'Requerido';
      if (!formData.cantidadMl) newErrors.cantidadMl = 'Requerido';
    }
    if (formData.tipo === 'solidos') {
      if (!formData.alimento) newErrors.alimento = 'Requerido';
      if (!formData.cantidadMl) newErrors.cantidadMl = 'Requerido';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = () => {
    if (!validate()) return;
    const payload = {
      ...formData,
      fechaHora: formData.inicio ? formData.inicio.format('YYYY-MM-DDTHH:mm') : null,
      duracionMin: formData.duracionMin ? Number(formData.duracionMin) : undefined,
    };
    delete payload.inicio;
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
            <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
            <TextField select name="tipo" value={formData.tipo} onChange={handleChange}>
              {tipos.map((t) => (
                <MenuItem key={t.value} value={t.value}>
                  {t.label}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Hora</FormLabel>
            <DateTimePicker
              value={formData.inicio}
              onChange={handleInicioChange}
              slotProps={{ textField: { fullWidth: true, error: !!errors.inicio, helperText: errors.inicio } }}
            />
          </FormControl>
          {formData.tipo === 'lactancia' && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Lado</FormLabel>
                <TextField
                  select
                  name="lado"
                  value={formData.lado}
                  onChange={handleChange}
                  error={!!errors.lado}
                  helperText={errors.lado}
                >
                  <MenuItem value="izquierdo">Izquierdo</MenuItem>
                  <MenuItem value="derecho">Derecho</MenuItem>
                </TextField>
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Duración (minutos)</FormLabel>
                <TextField
                  type="number"
                  name="duracionMin"
                  value={formData.duracionMin}
                  onChange={handleChange}
                  error={!!errors.duracionMin}
                  helperText={errors.duracionMin}
                  inputProps={{ min: 0 }}
                />
              </FormControl>
            </>
          )}
          {formData.tipo === 'biberon' && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
                <TextField
                  select
                  name="tipoLeche"
                  value={formData.tipoLeche}
                  onChange={handleChange}
                  error={!!errors.tipoLeche}
                  helperText={errors.tipoLeche}
                >
                  <MenuItem value="leche_materna">Leche materna extraída</MenuItem>
                  <MenuItem value="formula">Fórmula</MenuItem>
                </TextField>
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Cantidad (ml)</FormLabel>
                <TextField
                  type="number"
                  name="cantidadMl"
                  value={formData.cantidadMl}
                  onChange={handleChange}
                  error={!!errors.cantidadMl}
                  helperText={errors.cantidadMl}
                  inputProps={{ min: 0 }}
                />
              </FormControl>
            </>
          )}
          {formData.tipo === 'solidos' && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Tipo de alimento</FormLabel>
                <TextField
                  name="alimento"
                  value={formData.alimento}
                  onChange={handleChange}
                  error={!!errors.alimento}
                  helperText={errors.alimento}
                />
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Cantidad</FormLabel>
                <TextField
                  name="cantidadMl"
                  value={formData.cantidadMl}
                  onChange={handleChange}
                  error={!!errors.cantidadMl}
                  helperText={errors.cantidadMl}
                />
              </FormControl>
            </>
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
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSubmit} variant="contained">
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}

