
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
import { listarTipos, listarTiposPanal } from '../../services/cuidadosService';
import { saveButton, cancelButton } from '../../theme/buttonStyles';

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

  useEffect(() => {
    if (initialData) {
      setFormData({
        inicio: initialData.inicio ? dayjs(initialData.inicio) : null,
        tipoId: initialData.tipoId || "",
        cantidadMl: initialData.cantidadMl || "",
        duracion: initialData.duracion || "",
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
      ["cantidadMl", "duracion", "cantidadPanal"].includes(name)
        ? String(Math.max(0, Number(value)))
        : value;
    setFormData((prev) => ({
      ...prev,
      [name]: sanitizedValue,
    }));
  };

  const handleInicioChange = (newValue) => {
    setFormData((prev) => ({ ...prev, inicio: newValue }));
  };

  const handleSubmit = () => {
    if (!formData.inicio) return;
    const payload = {
      ...formData,
      cantidadPanal: formData.cantidadPanal,
      inicio: formData.inicio ? formData.inicio.toISOString() : null,
    };
    onSubmit(payload);
  };

  const selectedTipo = tipoOptions.find(
    (option) => Number(option.id) === Number(formData.tipoId),
  );
  const isSueno = selectedTipo?.nombre === "Sueño";
  const isPanal = selectedTipo?.nombre === "Pañal";

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
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Duración</FormLabel>
              <TextField
                type="number"
                name="duracion"
                value={formData.duracion}
                onChange={handleChange}
                inputProps={{ min: 0 }}
              />
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
