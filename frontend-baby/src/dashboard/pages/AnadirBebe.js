import React, { useRef, useState, useContext } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { crearBebe } from '../../services/bebesService';
import { BabyContext } from '../../context/BabyContext';
import CircularProgress from '@mui/material/CircularProgress';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

export default function AnadirBebe({ open, onClose }) {
  const { addBaby } = useContext(BabyContext);
  const fileInputRef = useRef(null);
  const [preview, setPreview] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    fechaNacimiento: null,
    sexo: 'ND',
    pesoNacer: '',
    tallaNacer: '',
    perimetroCranealNacer: '',
    semanasGestacion: '',
    imagenBebe: null,
    numeroSs: '',
    grupoSanguineo: '',
    medicaciones: '',
    alergias: '',
    pediatra: '',
    centroMedico: '',
    telefonoCentroMedico: '',
    observaciones: '',
  });
  const [loading, setLoading] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);

  const handleCloseSnackbar = (_, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpenSnackbar(false);
    onClose();
  };

  const gruposSanguineos = ['O+', 'O-', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-'];
  const alergiasOptions = ['Ninguna', 'Gluten', 'Lactosa', 'Frutos secos', 'Polen', 'Ácaros', 'Medicamentos'];

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleDateChange = (newValue) => {
    setFormData((prev) => ({ ...prev, fechaNacimiento: newValue }));
  };

  const handlePhotoChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      setFormData((prev) => ({ ...prev, imagenBebe: file }));
      setPreview(URL.createObjectURL(file));
    }
  };

  const toBase64 = (file) =>
    new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result.split(',')[1]);
      reader.onerror = (error) => reject(error);
    });

  const handleSubmit = async () => {
    const payload = {};
    await Promise.all(
      Object.entries(formData).map(async ([key, value]) => {
        if (value !== null && value !== '') {
          if (dayjs.isDayjs(value)) {
            payload[key] = value.format('YYYY-MM-DD');
          } else if (key === 'imagenBebe' && value instanceof File) {
            payload[key] = await toBase64(value);
          } else {
            payload[key] = value;
          }
        }
      })
    );

    setLoading(true);
    try {
      const response = await crearBebe(payload);
      addBaby(response.data);
      setOpenSnackbar(true);
    } catch (error) {
      console.error('Error creating baby:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>Añadir bebé</DialogTitle>
        <DialogContent>
          <Stack sx={{ mt: 1 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Nombre del bebé</FormLabel>
              <TextField
                required
                name="nombre"
                value={formData.nombre}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Fecha de nacimiento</FormLabel>
              <DatePicker
                value={formData.fechaNacimiento}
                onChange={handleDateChange}
                disabled={loading}
                slotProps={{ textField: { fullWidth: true, disabled: loading } }}
              />
            </FormControl>
            <FormControl sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Sexo</FormLabel>
              <RadioGroup
                row
                name="sexo"
                value={formData.sexo}
                onChange={handleChange}
              >
                <FormControlLabel value="M" control={<Radio />} label="M" disabled={loading} />
                <FormControlLabel value="F" control={<Radio />} label="F" disabled={loading} />
                <FormControlLabel value="ND" control={<Radio />} label="ND" disabled={loading} />
              </RadioGroup>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Peso al nacer (kg)</FormLabel>
              <TextField
                type="number"
                name="pesoNacer"
                value={formData.pesoNacer}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Talla al nacer (cm)</FormLabel>
              <TextField
                type="number"
                name="tallaNacer"
                value={formData.tallaNacer}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Perímetro craneal al nacer (cm)</FormLabel>
              <TextField
                type="number"
                name="perimetroCranealNacer"
                value={formData.perimetroCranealNacer}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Semanas de gestación</FormLabel>
              <TextField
                type="number"
                name="semanasGestacion"
                value={formData.semanasGestacion}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Grupo sanguíneo</FormLabel>
              <TextField
                select
                name="grupoSanguineo"
                value={formData.grupoSanguineo}
                onChange={handleChange}
                disabled={loading}
              >
                {gruposSanguineos.map((grupo) => (
                  <MenuItem key={grupo} value={grupo}>
                    {grupo}
                  </MenuItem>
                ))}
              </TextField>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Alergias</FormLabel>
              <TextField
                select
                name="alergias"
                value={formData.alergias}
                onChange={handleChange}
                disabled={loading}
              >
                {alergiasOptions.map((alergia) => (
                  <MenuItem key={alergia} value={alergia}>
                    {alergia}
                  </MenuItem>
                ))}
              </TextField>
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Medicaciones</FormLabel>
              <TextField
                name="medicaciones"
                value={formData.medicaciones}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Número SS</FormLabel>
              <TextField
                name="numeroSs"
                value={formData.numeroSs}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Pediatra</FormLabel>
              <TextField
                name="pediatra"
                value={formData.pediatra}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Centro médico</FormLabel>
              <TextField
                name="centroMedico"
                value={formData.centroMedico}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Número centro médico</FormLabel>
              <TextField
                name="telefonoCentroMedico"
                value={formData.telefonoCentroMedico}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Observaciones</FormLabel>
              <TextField
                multiline
                rows={4}
                name="observaciones"
                value={formData.observaciones}
                onChange={handleChange}
                disabled={loading}
              />
            </FormControl>
            <FormControl sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Foto/Identidad</FormLabel>
              <Stack spacing={2} alignItems="center">
                <Avatar src={preview} sx={{ width: 120, height: 120 }} />
                <input
                  type="file"
                  accept="image/*"
                  ref={fileInputRef}
                  style={{ display: 'none' }}
                  onChange={handlePhotoChange}
                  disabled={loading}
                />
                <Button
                  variant="contained"
                  onClick={() => fileInputRef.current && fileInputRef.current.click()}
                  disabled={loading}
                >
                  Subir foto
                </Button>
              </Stack>
            </FormControl>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={onClose} disabled={loading}>
            Cancelar
          </Button>
          <Button onClick={handleSubmit} variant="contained" disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Guardar'}
          </Button>
        </DialogActions>
        <Snackbar
          open={openSnackbar}
          autoHideDuration={6000}
          onClose={handleCloseSnackbar}
        >
          <Alert
            onClose={handleCloseSnackbar}
            severity="success"
            sx={{ width: '100%' }}
          >
            Bebé guardado correctamente
          </Alert>
        </Snackbar>
      </Dialog>
    </LocalizationProvider>
  );
}

