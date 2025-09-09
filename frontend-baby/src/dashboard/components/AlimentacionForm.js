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
import { saveButton, cancelButton } from '../../theme/buttonStyles';
import { listarTiposLactancia } from '../../services/alimentacionService';

const tipos = [
  { value: 'lactancia', label: 'Lactancia' },
  { value: 'biberon', label: 'Biberón' },
  { value: 'solidos', label: 'Sólidos' },
];

export default function AlimentacionForm({ open, onClose, onSubmit, initialData }) {
  const [tiposLactancia, setTiposLactancia] = useState([]);
  const [formData, setFormData] = useState({
    tipo: 'lactancia',
    inicio: null,
    lado: '',
    duracionMin: '',
    tipoLactanciaId: '',
    tipoLeche: '',
    cantidadMl: '',
    cantidadLecheFormula: '',
    alimento: '',
    cantidadOtrosAlimentos: '',
    observaciones: '',
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    listarTiposLactancia()
      .then((res) => setTiposLactancia(res.data))
      .catch((err) => console.error('Error fetching tipos lactancia:', err));
  }, []);

  useEffect(() => {
    if (initialData) {
      setFormData({
        tipo: initialData.tipo || 'lactancia',
        inicio: initialData.inicio ? dayjs(initialData.inicio) : null,
        lado: initialData.lado || '',
        duracionMin: initialData.duracionMin || '',
        tipoLactanciaId: initialData.tipoLactancia?.id || '',
        tipoLeche: initialData.tipoLeche || '',
        cantidadMl: initialData.cantidadMl || '',
        cantidadLecheFormula: initialData.cantidadLecheFormula || '',
        alimento: initialData.alimento || '',
        cantidadOtrosAlimentos: initialData.cantidadOtrosAlimentos || '',
        observaciones: initialData.observaciones || '',
      });
    } else {
      setFormData({
        tipo: initialData?.tipo || 'lactancia',
        inicio: null,
        lado: '',
        duracionMin: '',
        tipoLactanciaId: '',
        tipoLeche: '',
        cantidadMl: '',
        cantidadLecheFormula: '',
        alimento: '',
        cantidadOtrosAlimentos: '',
        observaciones: '',
      });
    }
    setErrors({});
  }, [initialData, open]);

  useEffect(() => {
    if (
      initialData?.tipoLactancia &&
      typeof initialData.tipoLactancia === 'string'
    ) {
      const found = tiposLactancia.find(
        (t) =>
          t.nombre?.toLowerCase() ===
          initialData.tipoLactancia.toLowerCase()
      );
      if (found) {
        setFormData((prev) => ({ ...prev, tipoLactanciaId: found.id }));
      }
    }
  }, [tiposLactancia, initialData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleInicioChange = (newValue) => {
    setFormData((prev) => ({ ...prev, inicio: newValue }));
  };

  const selectedTipo = tiposLactancia.find(
    (t) => t.id === Number(formData.tipoLactanciaId)
  )?.nombre?.toLowerCase();

  const validate = () => {
    const newErrors = {};
    if (!formData.inicio) newErrors.inicio = 'Requerido';
    if (formData.tipo === 'lactancia') {
      if (!formData.tipoLactanciaId) newErrors.tipoLactanciaId = 'Requerido';
      if (!formData.lado) newErrors.lado = 'Requerido';
      if (!formData.duracionMin) newErrors.duracionMin = 'Requerido';
      if (selectedTipo?.includes('complementaria') && !formData.alimento)
        newErrors.alimento = 'Requerido';
      if (selectedTipo?.includes('mixta') && !formData.cantidadLecheFormula)
        newErrors.cantidadLecheFormula = 'Requerido';
      if (
        selectedTipo?.includes('predominante') &&
        !formData.cantidadOtrosAlimentos
      )
        newErrors.cantidadOtrosAlimentos = 'Requerido';
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
      fechaHora: formData.inicio
        ? formData.inicio.format('YYYY-MM-DDTHH:mm')
        : null,
      duracionMin: formData.duracionMin
        ? Number(formData.duracionMin)
        : undefined,
      cantidadLecheFormula: formData.cantidadLecheFormula
        ? Number(formData.cantidadLecheFormula)
        : undefined,
      cantidadOtrosAlimentos: formData.cantidadOtrosAlimentos
        ? Number(formData.cantidadOtrosAlimentos)
        : undefined,
      tipoLactancia: formData.tipoLactanciaId
        ? { id: formData.tipoLactanciaId }
        : undefined,
    };
    delete payload.inicio;
    delete payload.tipoLactanciaId;
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
            <TextField
              select
              name="tipo"
              value={formData.tipo}
              onChange={handleChange}
              disabled={initialData?.disableTipo}
            >
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
                <FormLabel sx={{ mb: 1 }}>Tipo lactancia</FormLabel>
                <TextField
                  select
                  name="tipoLactanciaId"
                  value={formData.tipoLactanciaId}
                  onChange={handleChange}
                  error={!!errors.tipoLactanciaId}
                  helperText={errors.tipoLactanciaId}
                  disabled={initialData?.disableTipoLactancia}
                >
                  {tiposLactancia.map((t) => (
                    <MenuItem key={t.id} value={t.id}>
                      {t.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>
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
              {selectedTipo?.includes('complementaria') && (
                <FormControl fullWidth sx={{ mb: 2 }}>
                  <FormLabel sx={{ mb: 1 }}>Sólido</FormLabel>
                  <TextField
                    name="alimento"
                    value={formData.alimento}
                    onChange={handleChange}
                    error={!!errors.alimento}
                    helperText={errors.alimento}
                  />
                </FormControl>
              )}
              {selectedTipo?.includes('mixta') && (
                <FormControl fullWidth sx={{ mb: 2 }}>
                  <FormLabel sx={{ mb: 1 }}>Cantidad leche fórmula</FormLabel>
                  <TextField
                    type="number"
                    name="cantidadLecheFormula"
                    value={formData.cantidadLecheFormula}
                    onChange={handleChange}
                    error={!!errors.cantidadLecheFormula}
                    helperText={errors.cantidadLecheFormula}
                    inputProps={{ min: 0 }}
                  />
                </FormControl>
              )}
              {selectedTipo?.includes('predominante') && (
                <FormControl fullWidth sx={{ mb: 2 }}>
                  <FormLabel sx={{ mb: 1 }}>Cantidad otros alimentos</FormLabel>
                  <TextField
                    type="number"
                    name="cantidadOtrosAlimentos"
                    value={formData.cantidadOtrosAlimentos}
                    onChange={handleChange}
                    error={!!errors.cantidadOtrosAlimentos}
                    helperText={errors.cantidadOtrosAlimentos}
                    inputProps={{ min: 0 }}
                  />
                </FormControl>
              )}
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
        <Button variant="contained" onClick={onClose} sx={cancelButton}>
          Cancelar
        </Button>
        <Button onClick={handleSubmit} variant="contained" sx={saveButton}>
          Guardar
        </Button>
      </DialogActions>
    </Dialog>
  );
}

