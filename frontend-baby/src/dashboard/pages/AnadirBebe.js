import React, { useRef, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import Switch from '@mui/material/Switch';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { crearBebe } from '../../services/bebesService';
import { BabyContext } from '../../context/BabyContext';
import CircularProgress from '@mui/material/CircularProgress';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

export default function AnadirBebe() {
  const navigate = useNavigate();
  const { addBaby } = useContext(BabyContext);
  const fileInputRef = useRef(null);
  const [preview, setPreview] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    fechaNacimiento: null,
    sexo: 'ND',
    bebeActivo: true,
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
    navigate(-1);
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

  const handleSubmit = async (e) => {
    e.preventDefault();
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
      <Box component="form" onSubmit={handleSubmit} sx={{ flexGrow: 1 }}>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Añadir bebé
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} md={8}>
            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Datos básicos
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    required
                    label="Nombre del bebé"
                    name="nombre"
                    fullWidth
                    value={formData.nombre}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <DatePicker
                    label="Fecha de nacimiento"
                    value={formData.fechaNacimiento}
                    onChange={handleDateChange}
                    disabled={loading}
                    slotProps={{ textField: { fullWidth: true, disabled: loading } }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControl>
                    <FormLabel>Sexo</FormLabel>
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
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={formData.bebeActivo}
                        onChange={handleChange}
                        name="bebeActivo"
                        disabled={loading}
                      />
                    }
                    label="Bebé activo"
                  />
                </Grid>
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Datos de nacimiento
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Peso al nacer (kg)"
                    name="pesoNacer"
                    type="number"
                    fullWidth
                    value={formData.pesoNacer}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Talla al nacer (cm)"
                    name="tallaNacer"
                    type="number"
                    fullWidth
                    value={formData.tallaNacer}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Perímetro craneal al nacer (cm)"
                    name="perimetroCranealNacer"
                    type="number"
                    fullWidth
                    value={formData.perimetroCranealNacer}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Semanas de gestación"
                    name="semanasGestacion"
                    type="number"
                    fullWidth
                    value={formData.semanasGestacion}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Salud
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={4}>
                  <TextField
                    select
                    label="Grupo sanguíneo"
                    name="grupoSanguineo"
                    fullWidth
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
                </Grid>
                <Grid item xs={12} sm={4}>
                  <TextField
                    select
                    label="Alergias"
                    name="alergias"
                    fullWidth
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
                </Grid>
                <Grid item xs={12} sm={4}>
                  <TextField
                    label="Medicaciones"
                    name="medicaciones"
                    fullWidth
                    value={formData.medicaciones}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Datos clínicos
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Número SS"
                    name="numeroSs"
                    fullWidth
                    value={formData.numeroSs}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Pediatra"
                    name="pediatra"
                    fullWidth
                    value={formData.pediatra}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Centro médico"
                    name="centroMedico"
                    fullWidth
                    value={formData.centroMedico}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Número centro médico"
                    name="telefonoCentroMedico"
                    fullWidth
                    value={formData.telefonoCentroMedico}
                    onChange={handleChange}
                    disabled={loading}
                  />
                </Grid>
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Observaciones
              </Typography>
              <TextField
                multiline
                rows={4}
                name="observaciones"
                fullWidth
                value={formData.observaciones}
                onChange={handleChange}
                disabled={loading}
              />
            </Box>
          </Grid>

          <Grid item xs={12} md={4}>
            <Box component={Paper} sx={{ p: 2, textAlign: 'center' }}>
              <Typography variant="h6" gutterBottom>
                Foto/Identidad
              </Typography>
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
            </Box>
          </Grid>
        </Grid>

        <Stack direction="row" spacing={2} justifyContent="flex-end" sx={{ mt: 2 }}>
          <Button onClick={() => navigate(-1)} disabled={loading}>
            Cancelar
          </Button>
          <Button type="submit" variant="contained" disabled={loading}>
            {loading ? <CircularProgress size={24} /> : 'Guardar'}
          </Button>
        </Stack>
      </Box>
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
    </LocalizationProvider>
  );
}

