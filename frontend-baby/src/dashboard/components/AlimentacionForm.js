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
import {
  listarTiposLactancia,
  listarTiposAlimentacion,
  listarTiposBiberon,
  listarTiposAlimentacionSolidos,
} from '../../services/alimentacionService';

export default function AlimentacionForm({ open, onClose, onSubmit, initialData }) {
  const [tiposAlimentacion, setTiposAlimentacion] = useState([]);
  const [tiposLactancia, setTiposLactancia] = useState([]);
  const [tiposBiberon, setTiposBiberon] = useState([]);
  const [tiposSolidos, setTiposSolidos] = useState([]);
  const [formData, setFormData] = useState({
    tipoAlimentacionId: '',
    inicio: null,
    lado: '',
    duracionMin: '',
    tipoLactanciaId: '',
    tipoBiberonId: '',
    cantidadMl: '',
    cantidadLecheFormula: '',
    tipoAlimentacionSolidoId: '',
    cantidad: '',
    cantidadOtrosAlimentos: '',
    alimentacionOtros: '',
    observaciones: '',
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    listarTiposAlimentacion()
      .then((res) => {
        setTiposAlimentacion(res.data);
        const lactancia = res.data.find(
          (t) => t.nombre?.toLowerCase() === 'lactancia'
        );
        setFormData((prev) => ({
          ...prev,
          tipoAlimentacionId:
            prev.tipoAlimentacionId || lactancia?.id || '',
        }));
      })
      .catch((err) =>
        console.error('Error fetching tipos alimentacion:', err)
      );
    listarTiposLactancia()
      .then((res) => setTiposLactancia(res.data))
      .catch((err) => console.error('Error fetching tipos lactancia:', err));
    listarTiposBiberon()
      .then((res) => setTiposBiberon(res.data))
      .catch((err) => console.error('Error fetching tipos biberon:', err));
    listarTiposAlimentacionSolidos()
      .then((res) => setTiposSolidos(res.data))
      .catch((err) =>
        console.error('Error fetching tipos alimentacion solidos:', err)
      );
  }, []);

  useEffect(() => {
    if (initialData) {
      setFormData({
        tipoAlimentacionId: initialData.tipoAlimentacion?.id || '',
        inicio: initialData.inicio ? dayjs(initialData.inicio) : null,
        lado: initialData.lado || '',
        duracionMin: initialData.duracionMin || '',
        tipoLactanciaId: initialData.tipoLactancia?.id || '',
        tipoBiberonId: initialData.tipoBiberon?.id || '',
        cantidadMl: initialData.cantidadMl || '',
        cantidadLecheFormula: initialData.cantidadLecheFormula || '',
        tipoAlimentacionSolidoId:
          initialData.tipoAlimentacionSolido?.id || '',
        cantidad: initialData.cantidad || '',
        cantidadOtrosAlimentos: initialData.cantidadOtrosAlimentos || '',
        alimentacionOtros: initialData.alimentacionOtros || '',
        observaciones: initialData.observaciones || '',
      });
    } else {
      setFormData({
        tipoAlimentacionId: '',
        inicio: null,
        lado: '',
        duracionMin: '',
        tipoLactanciaId: '',
        tipoBiberonId: '',
        cantidadMl: '',
        cantidadLecheFormula: '',
        tipoAlimentacionSolidoId: '',
        cantidad: '',
        cantidadOtrosAlimentos: '',
        alimentacionOtros: '',
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

  const selectedTipoNombre = tiposAlimentacion.find(
    (t) => t.id === Number(formData.tipoAlimentacionId)
  )?.nombre;
  const selectedTipo = selectedTipoNombre
    ? selectedTipoNombre
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
    : '';
  const selectedTipoLactancia = tiposLactancia.find(
    (t) => t.id === Number(formData.tipoLactanciaId)
  )?.nombre?.toLowerCase();
  const selectedSolidoNombre = tiposSolidos.find(
    (t) => t.id === Number(formData.tipoAlimentacionSolidoId)
  )?.nombre;
  const isSolidoOtros = selectedSolidoNombre
    ? selectedSolidoNombre
        .toLowerCase()
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '') === 'otros'
    : false;

  const validate = () => {
    const newErrors = {};
    if (!formData.inicio) newErrors.inicio = 'Requerido';
    if (!formData.tipoAlimentacionId)
      newErrors.tipoAlimentacionId = 'Requerido';
    if (selectedTipo === 'lactancia') {
      if (!formData.tipoLactanciaId) newErrors.tipoLactanciaId = 'Requerido';
      if (!formData.lado) newErrors.lado = 'Requerido';
      if (!formData.duracionMin) newErrors.duracionMin = 'Requerido';
      if (selectedTipoLactancia?.includes('complementaria')) {
        if (!formData.tipoAlimentacionSolidoId)
          newErrors.tipoAlimentacionSolidoId = 'Requerido';
        if (isSolidoOtros && !formData.alimentacionOtros)
          newErrors.alimentacionOtros = 'Requerido';
      }
      if (
        selectedTipoLactancia?.includes('mixta') &&
        !formData.cantidadLecheFormula
      )
        newErrors.cantidadLecheFormula = 'Requerido';
      if (
        selectedTipoLactancia?.includes('predominante') &&
        !formData.cantidadOtrosAlimentos
      )
        newErrors.cantidadOtrosAlimentos = 'Requerido';
    }
    if (selectedTipo === 'biberon') {
      if (!formData.tipoBiberonId) newErrors.tipoBiberonId = 'Requerido';
      if (!formData.cantidadMl) newErrors.cantidadMl = 'Requerido';
    }
    if (selectedTipo === 'solidos') {
      if (!formData.tipoAlimentacionSolidoId)
        newErrors.tipoAlimentacionSolidoId = 'Requerido';
      if (!formData.cantidad) newErrors.cantidad = 'Requerido';
      if (isSolidoOtros && !formData.alimentacionOtros)
        newErrors.alimentacionOtros = 'Requerido';
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
      cantidadMl: formData.cantidadMl
        ? Number(formData.cantidadMl)
        : undefined,
      cantidadLecheFormula: formData.cantidadLecheFormula
        ? Number(formData.cantidadLecheFormula)
        : undefined,
      cantidadOtrosAlimentos: formData.cantidadOtrosAlimentos
        ? Number(formData.cantidadOtrosAlimentos)
        : undefined,
      tipoAlimentacionId: formData.tipoAlimentacionId
        ? Number(formData.tipoAlimentacionId)
        : undefined,
      tipoLactanciaId: formData.tipoLactanciaId
        ? Number(formData.tipoLactanciaId)
        : undefined,
      tipoBiberonId: formData.tipoBiberonId
        ? Number(formData.tipoBiberonId)
        : undefined,
      tipoAlimentacionSolidoId: formData.tipoAlimentacionSolidoId
        ? Number(formData.tipoAlimentacionSolidoId)
        : undefined,
    };
    if (!isSolidoOtros) delete payload.alimentacionOtros;
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
            <TextField
              select
              name="tipoAlimentacionId"
              value={formData.tipoAlimentacionId}
              onChange={handleChange}
              disabled={initialData?.disableTipo}
              error={!!errors.tipoAlimentacionId}
              helperText={errors.tipoAlimentacionId}
            >
              {tiposAlimentacion.map((t) => (
                <MenuItem key={t.id} value={t.id}>
                  {t.nombre}
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
          {selectedTipo === 'lactancia' && (
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
              {selectedTipoLactancia?.includes('complementaria') && (
                <>
                  <FormControl fullWidth sx={{ mb: 2 }}>
                    <FormLabel sx={{ mb: 1 }}>Sólido</FormLabel>
                    <TextField
                      select
                      name="tipoAlimentacionSolidoId"
                      value={formData.tipoAlimentacionSolidoId}
                      onChange={handleChange}
                      error={!!errors.tipoAlimentacionSolidoId}
                      helperText={errors.tipoAlimentacionSolidoId}
                    >
                      {tiposSolidos.map((t) => (
                        <MenuItem key={t.id} value={t.id}>
                          {t.nombre}
                        </MenuItem>
                      ))}
                    </TextField>
                  </FormControl>
                  {isSolidoOtros && (
                    <FormControl fullWidth sx={{ mb: 2 }}>
                      <FormLabel sx={{ mb: 1 }}>Especificar</FormLabel>
                      <TextField
                        name="alimentacionOtros"
                        value={formData.alimentacionOtros}
                        onChange={handleChange}
                        error={!!errors.alimentacionOtros}
                        helperText={errors.alimentacionOtros}
                      />
                    </FormControl>
                  )}
                </>
              )}
              {selectedTipoLactancia?.includes('mixta') && (
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
              {selectedTipoLactancia?.includes('predominante') && (
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
          {selectedTipo === 'biberon' && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Tipo</FormLabel>
                <TextField
                  select
                  name="tipoBiberonId"
                  value={formData.tipoBiberonId}
                  onChange={handleChange}
                  error={!!errors.tipoBiberonId}
                  helperText={errors.tipoBiberonId}
                >
                  {tiposBiberon.map((t) => (
                    <MenuItem key={t.id} value={t.id}>
                      {t.nombre}
                    </MenuItem>
                  ))}
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
          {selectedTipo === 'solidos' && (
            <>
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Tipo de alimento</FormLabel>
                <TextField
                  select
                  name="tipoAlimentacionSolidoId"
                  value={formData.tipoAlimentacionSolidoId}
                  onChange={handleChange}
                  error={!!errors.tipoAlimentacionSolidoId}
                  helperText={errors.tipoAlimentacionSolidoId}
                >
                  {tiposSolidos.map((t) => (
                    <MenuItem key={t.id} value={t.id}>
                      {t.nombre}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>
              {isSolidoOtros && (
                <FormControl fullWidth sx={{ mb: 2 }}>
                  <FormLabel sx={{ mb: 1 }}>Especificar</FormLabel>
                  <TextField
                    name="alimentacionOtros"
                    value={formData.alimentacionOtros}
                    onChange={handleChange}
                    error={!!errors.alimentacionOtros}
                    helperText={errors.alimentacionOtros}
                  />
                </FormControl>
              )}
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Cantidad</FormLabel>
                <TextField
                  name="cantidad"
                  value={formData.cantidad}
                  onChange={handleChange}
                  error={!!errors.cantidad}
                  helperText={errors.cantidad}
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

