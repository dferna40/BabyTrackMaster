
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
import FormHelperText from '@mui/material/FormHelperText';
import { DateTimePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import { listarTipos, listarTiposPanal } from '../../services/cuidadosService';
import { saveButton, cancelButton } from '../../theme/buttonStyles';

const duracionOptions = [
  '15m',
  '30m',
  '1h',
  '1h30m',
  '2h',
  '2h30m',
  '3h',
  '3h30m',
  '4h',
  '4h30m',
  '5h',
  '5h30m',
  '6h',
  '6h30m',
  '7h',
  '7h30m',
  '8h',
  '8h30m',
  '9h',
  '9h30m',
  '10h',
  '10h30m',
  '11h',
  '11h30m',
  '12h',
];

export default function CuidadoForm({ open, onClose, onSubmit, initialData }) {
  const disableTipo = initialData?.disableTipo;
  const [formData, setFormData] = useState({
    inicio: null,
    tipoId: "",
    cantidadMl: "",
    duracion: "",
    observaciones: "",
    tipoPanalId: "",
    cantidadPanal: "",
  });
  const [tipoOptions, setTipoOptions] = useState([]);
  const [tipoPanalOptions, setTipoPanalOptions] = useState([]);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setFormData({
        inicio: initialData.inicio ? dayjs(initialData.inicio) : null,
        tipoId: initialData.tipoId || "",
        cantidadMl: initialData.cantidadMl || "",
        duracion: initialData.duracion ? String(initialData.duracion) : "",
        observaciones: initialData.observaciones || "",
        tipoPanalId: initialData.tipoPanalId || "",
        cantidadPanal: initialData.cantidadPanal || "",
      });
    } else {
      setFormData({
        inicio: null,
        tipoId: "",
        cantidadMl: "",
        duracion: "",
        observaciones: "",
        tipoPanalId: "",
        cantidadPanal: "",
      });
    }
    setErrors({});
  }, [initialData, open]);

  useEffect(() => {
    listarTipos()
      .then((response) => setTipoOptions(response.data))
      .catch((err) => console.error("Error fetching tipos cuidado:", err));
    listarTiposPanal()
      .then((response) => setTipoPanalOptions(response.data))
      .catch((err) => console.error("Error fetching tipos panal:", err));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    const sanitizedValue =
      ["cantidadMl", "cantidadPanal"].includes(name)
        ? String(Math.max(0, Number(value)))
        : value;
    setFormData((prev) => ({
      ...prev,
      [name]: sanitizedValue,
    }));
  };

  const handleInicioChange = (newValue) => {
    if (newValue && dayjs(newValue).isAfter(dayjs())) {
      setErrors((prev) => ({ ...prev, inicio: 'La fecha no puede ser futura' }));
      return;
    }
    setErrors((prev) => ({ ...prev, inicio: undefined }));
    setFormData((prev) => ({ ...prev, inicio: newValue }));
  };

  const selectedTipo = tipoOptions.find(
    (option) => Number(option.id) === Number(formData.tipoId),
  );
  const isSueno = selectedTipo?.nombre === "Sueño";
  const isPanal = selectedTipo?.nombre === "Pañal";

  const handleSubmit = () => {
    if (!formData.inicio) return;
    const newErrors = {};
    if (formData.inicio && dayjs(formData.inicio).isAfter(dayjs())) {
      newErrors.inicio = 'La fecha no puede ser futura';
    }
    if (isSueno && !formData.duracion) {
      newErrors.duracion = 'La duración es obligatoria';
    }
    setErrors(newErrors);
    if (Object.keys(newErrors).length > 0) return;
    const payload = {
      ...formData,
      cantidadPanal: formData.cantidadPanal,
      inicio: formData.inicio ? formData.inicio.toISOString() : null,
    };
    onSubmit(payload);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>
        {initialData && initialData.id
          ? "Editar cuidado"
          : "Añadir nuevo cuidado"}
      </DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
          <FormLabel sx={{ mb: 1 }}>Inicio</FormLabel>
          <DateTimePicker
              value={formData.inicio}
              onChange={handleInicioChange}
              disableFuture
              slotProps={{
                textField: {
                  fullWidth: true,
                  required: true,
                  error: !!errors.inicio,
                  helperText: errors.inicio,
                },
              }}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
            <TextField
              select
              name="tipoId"
              value={formData.tipoId}
              onChange={handleChange}
              disabled={disableTipo}
            >
              {tipoOptions.map((option) => (
                <MenuItem key={option.id} value={option.id}>
                  {option.nombre}
                </MenuItem>
              ))}
            </TextField>
          </FormControl>
          {isSueno ? (
            <FormControl fullWidth sx={{ mb: 2 }} error={!!errors.duracion}>
              <FormLabel sx={{ mb: 1 }}>Duración</FormLabel>
              <TextField
                select
                name="duracion"
                value={formData.duracion}
                onChange={handleChange}
                error={!!errors.duracion}
              >
                {duracionOptions.map((option) => (
                  <MenuItem key={option} value={option}>
                    {option}
                  </MenuItem>
                ))}
              </TextField>
              <FormHelperText>
                {errors.duracion || 'Selecciona la duración'}
              </FormHelperText>
            </FormControl>
          ) : isPanal ? null : (
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
          )}
          {isPanal && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Tipo pañal</FormLabel>
                <TextField
                  select
                  name="tipoPanalId"
                  value={formData.tipoPanalId}
                  onChange={handleChange}
                >
                  {tipoPanalOptions.map((option) => (
                    <MenuItem key={option.id} value={option.id}>
                      {option.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Cantidad pañal</FormLabel>
                <TextField
                  type="number"
                  name="cantidadPanal"
                  value={formData.cantidadPanal}
                  onChange={handleChange}
                  inputProps={{ min: 0 }}
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
          <Button
            variant="contained"
            onClick={onClose}
            sx={cancelButton}
          >
            Cancelar
          </Button>
          <Button
            onClick={handleSubmit}
            variant="contained"
            sx={saveButton}
          >
            Guardar
          </Button>
        </DialogActions>
      </Dialog>
  );
}
