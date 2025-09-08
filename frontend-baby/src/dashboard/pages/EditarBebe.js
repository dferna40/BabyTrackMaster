import React, { useContext, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import FormLabel from '@mui/material/FormLabel';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { DatePicker } from '@mui/x-date-pickers';
import dayjs from 'dayjs';
import { actualizarBebe, eliminarBebe, fetchTipoAlergias, fetchTipoGrupoSanguineo } from '../../services/bebesService';
import { BabyContext } from '../../context/BabyContext';
import CircularProgress from '@mui/material/CircularProgress';
import Snackbar from '@mui/material/Snackbar';
import Alert from '@mui/material/Alert';

export default function EditarBebe() {
  const navigate = useNavigate();
  const { activeBaby, setActiveBaby, removeBaby } = useContext(BabyContext);
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
    tipoGrupoSanguineoId: '',
    medicaciones: '',
    tipoAlergiaId: '',
    pediatra: '',
    centroMedico: '',
    telefonoCentroMedico: '',
    observaciones: '',
  });
  const [loading, setLoading] = useState(false);
  const [openSnackbar, setOpenSnackbar] = useState(false);

  useEffect(() => {
    if (activeBaby) {
      setFormData({
        nombre: activeBaby.nombre || '',
        fechaNacimiento: activeBaby.fechaNacimiento ? dayjs(activeBaby.fechaNacimiento) : null,
        sexo: activeBaby.sexo || 'ND',
        pesoNacer: activeBaby.pesoNacer || '',
        tallaNacer: activeBaby.tallaNacer || '',
        perimetroCranealNacer: activeBaby.perimetroCranealNacer || '',
        semanasGestacion: activeBaby.semanasGestacion || '',
        imagenBebe: null,
        numeroSs: activeBaby.numeroSs || '',
        tipoGrupoSanguineoId: activeBaby.tipoGrupoSanguineoId || '',
        medicaciones: activeBaby.medicaciones || '',
        tipoAlergiaId: activeBaby.tipoAlergiaId || '',
        pediatra: activeBaby.pediatra || '',
        centroMedico: activeBaby.centroMedico || '',
        telefonoCentroMedico: activeBaby.telefonoCentroMedico || '',
        observaciones: activeBaby.observaciones || '',
      });
      if (activeBaby.imagenBebe) {
        setPreview(`data:image/*;base64,${activeBaby.imagenBebe}`);
      }
    }
  }, [activeBaby]);

  const handleCloseSnackbar = (_, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setOpenSnackbar(false);
    navigate(-1);
  };

  const [gruposSanguineos, setGruposSanguineos] = useState([]);
  const [alergiasOptions, setAlergiasOptions] = useState([]);

  useEffect(() => {
    fetchTipoGrupoSanguineo()
      .then((res) => setGruposSanguineos(res.data))
      .catch((err) => console.error('Error fetching tipos grupo sanguíneo:', err));
    fetchTipoAlergias()
      .then((res) => setAlergiasOptions(res.data))
      .catch((err) => console.error('Error fetching tipos alergia:', err));
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    if (type === 'number' && Number(value) < 0) return;
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
          } else if (key === 'tipoGrupoSanguineoId' || key === 'tipoAlergiaId') {
            payload[key] = Number(value);
          } else {
            payload[key] = value;
          }
        }
      })
    );

    if (!activeBaby?.id) return;

    setLoading(true);
    try {
      const response = await actualizarBebe(activeBaby.id, payload);
      setActiveBaby(response.data);
      setOpenSnackbar(true);
    } catch (error) {
      console.error('Error updating baby:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!activeBaby?.id) return;
    setLoading(true);
    try {
      await eliminarBebe(activeBaby.id);
      removeBaby(activeBaby.id);
      navigate(-1);
    } catch (error) {
      console.error('Error deleting baby:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ flexGrow: 1 }}>
        <Typography variant="h4" sx={{ mb: 2 }}>
          Editar/borrar bebé
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
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.nombre}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <DatePicker
                    label="Fecha de nacimiento"
                    value={formData.fechaNacimiento}
                    onChange={handleDateChange}
                    disabled={loading}
                    slotProps={{
                      textField: {
                        required: true,
                        variant: 'outlined',
                        sx: {
                          '& .MuiOutlinedInput-root': { borderRadius: 1 },
                          '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                        },
                        fullWidth: true,
                        disabled: loading,
                        InputLabelProps: { shrink: true },
                      },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControl required>
                    <FormLabel required>Sexo</FormLabel>
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
                    inputProps={{ min: 0 }}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.pesoNacer}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Talla al nacer (cm)"
                    name="tallaNacer"
                    type="number"
                    inputProps={{ min: 0 }}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.tallaNacer}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Perímetro craneal al nacer (cm)"
                    name="perimetroCranealNacer"
                    type="number"
                    inputProps={{ min: 0 }}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.perimetroCranealNacer}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Semanas de gestación"
                    name="semanasGestacion"
                    type="number"
                    inputProps={{ min: 0 }}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.semanasGestacion}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Salud
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <FormControl
                    fullWidth
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                      minWidth: 160,
                      '& .MuiOutlinedInput-notchedOutline': {
                        borderColor: 'divider',
                        borderRadius: 1,
                      },
                    }}
                  >
                    <InputLabel id="grupo-sanguineo-label" shrink>
                      Grupo sanguíneo
                    </InputLabel>
                    <Select
                      labelId="grupo-sanguineo-label"
                      label="Grupo sanguíneo"
                      name="tipoGrupoSanguineoId"
                      value={formData.tipoGrupoSanguineoId}
                      onChange={handleChange}
                      disabled={loading}
                      fullWidth
                    >
                      {gruposSanguineos.map((grupo) => (
                        <MenuItem key={grupo.id} value={grupo.id}>
                          {grupo.nombre}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControl
                    fullWidth
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                      minWidth: 160,
                      '& .MuiOutlinedInput-notchedOutline': {
                        borderColor: 'divider',
                        borderRadius: 1,
                      },
                    }}
                  >
                    <InputLabel id="alergias-label" shrink>
                      Alergias
                    </InputLabel>
                    <Select
                      labelId="alergias-label"
                      label="Alergias"
                      name="tipoAlergiaId"
                      value={formData.tipoAlergiaId}
                      onChange={handleChange}
                      disabled={loading}
                      fullWidth
                    >
                      {alergiasOptions.map((alergia) => (
                        <MenuItem key={alergia.id} value={alergia.id}>
                          {alergia.nombre}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label="Medicaciones"
                    name="medicaciones"
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.medicaciones}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
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
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.numeroSs}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Pediatra"
                    name="pediatra"
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.pediatra}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Centro médico"
                    name="centroMedico"
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.centroMedico}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Número centro médico"
                    name="telefonoCentroMedico"
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                    value={formData.telefonoCentroMedico}
                    onChange={handleChange}
                    disabled={loading}
                    variant="outlined"
                    sx={{
                      '& .MuiOutlinedInput-root': { borderRadius: 1 },
                      '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                    }}
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
                variant="outlined"
                sx={{
                  '& .MuiOutlinedInput-root': { borderRadius: 1 },
                  '& .MuiOutlinedInput-notchedOutline': { borderColor: 'divider' },
                }}
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
                  sx={{
                    bgcolor: '#0d6efd',
                    '&:hover': { bgcolor: '#0b5ed7' },
                  }}
                >
                  Subir foto
                </Button>
              </Stack>
            </Box>
          </Grid>
        </Grid>

        <Stack direction="row" spacing={2} justifyContent="flex-end" sx={{ mt: 2 }}>
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            disabled={loading}
            sx={{ bgcolor: '#6c757d', '&:hover': { bgcolor: '#5c636a' } }}
          >
            Cancelar
          </Button>
          <Button
            variant="contained"
            onClick={handleDelete}
            disabled={loading}
            sx={{ bgcolor: '#dc3545', '&:hover': { bgcolor: '#bb2d3b' } }}
          >
            Eliminar bebé
          </Button>
          <Button
            type="submit"
            variant="contained"
            disabled={loading}
            sx={{ bgcolor: '#198754', '&:hover': { bgcolor: '#157347' } }}
          >
            {loading ? <CircularProgress size={24} /> : 'Guardar'}
          </Button>
        </Stack>
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
            Bebé actualizado correctamente
          </Alert>
        </Snackbar>
    </Box>
  );
}

