import React, { useRef, useState } from 'react';
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
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { crearBebe } from '../../services/bebesService';

export default function AnadirBebe() {
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const [preview, setPreview] = useState(null);

  const positiveNumber = Yup.number()
    .nullable()
    .transform((value, originalValue) => (originalValue === '' ? null : value))
    .typeError('Debe ser un número válido')
    .min(0, 'Debe ser mayor o igual a 0');

  const formik = useFormik({
    initialValues: {
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
    },
    validationSchema: Yup.object({
      nombre: Yup.string().required('El nombre es obligatorio'),
      fechaNacimiento: Yup.mixed()
        .required('La fecha de nacimiento es obligatoria')
        .test('is-valid', 'Fecha inválida', (value) => dayjs.isDayjs(value)),
      pesoNacer: positiveNumber,
      tallaNacer: positiveNumber,
      perimetroCranealNacer: positiveNumber,
      semanasGestacion: positiveNumber,
    }),
    onSubmit: async (values) => {
      const payload = {};
      await Promise.all(
        Object.entries(values).map(async ([key, value]) => {
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

      crearBebe(payload)
        .then(() => navigate(-1))
        .catch((error) => {
          console.error('Error creating baby:', error);
        });
    },
  });

  const gruposSanguineos = ['O+', 'O-', 'A+', 'A-', 'B+', 'B-', 'AB+', 'AB-'];
  const alergiasOptions = ['Ninguna', 'Gluten', 'Lactosa', 'Frutos secos', 'Polen', 'Ácaros', 'Medicamentos'];

  const handlePhotoChange = (e) => {
    const file = e.target.files?.[0];
    if (file) {
      formik.setFieldValue('imagenBebe', file);
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

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box component="form" onSubmit={formik.handleSubmit} sx={{ flexGrow: 1 }}>
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
                    label="Nombre del bebé"
                    name="nombre"
                    fullWidth
                    value={formik.values.nombre}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.nombre && Boolean(formik.errors.nombre)}
                    helperText={formik.touched.nombre && formik.errors.nombre}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <DatePicker
                    label="Fecha de nacimiento"
                    value={formik.values.fechaNacimiento}
                    onChange={(newValue) => formik.setFieldValue('fechaNacimiento', newValue)}
                    slotProps={{
                      textField: {
                        fullWidth: true,
                        name: 'fechaNacimiento',
                        onBlur: formik.handleBlur,
                        error:
                          formik.touched.fechaNacimiento &&
                          Boolean(formik.errors.fechaNacimiento),
                        helperText:
                          formik.touched.fechaNacimiento &&
                          formik.errors.fechaNacimiento,
                      },
                    }}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControl>
                    <FormLabel>Sexo</FormLabel>
                    <RadioGroup
                      row
                      name="sexo"
                      value={formik.values.sexo}
                      onChange={formik.handleChange}
                    >
                      <FormControlLabel value="M" control={<Radio />} label="M" />
                      <FormControlLabel value="F" control={<Radio />} label="F" />
                      <FormControlLabel value="ND" control={<Radio />} label="ND" />
                    </RadioGroup>
                  </FormControl>
                </Grid>
                <Grid item xs={12} sm={6}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={formik.values.bebeActivo}
                        onChange={(e) =>
                          formik.setFieldValue('bebeActivo', e.target.checked)
                        }
                        name="bebeActivo"
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
                    value={formik.values.pesoNacer}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.pesoNacer && Boolean(formik.errors.pesoNacer)}
                    helperText={formik.touched.pesoNacer && formik.errors.pesoNacer}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Talla al nacer (cm)"
                    name="tallaNacer"
                    type="number"
                    fullWidth
                    value={formik.values.tallaNacer}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.tallaNacer && Boolean(formik.errors.tallaNacer)}
                    helperText={formik.touched.tallaNacer && formik.errors.tallaNacer}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Perímetro craneal al nacer (cm)"
                    name="perimetroCranealNacer"
                    type="number"
                    fullWidth
                    value={formik.values.perimetroCranealNacer}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.perimetroCranealNacer &&
                      Boolean(formik.errors.perimetroCranealNacer)
                    }
                    helperText={
                      formik.touched.perimetroCranealNacer &&
                      formik.errors.perimetroCranealNacer
                    }
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Semanas de gestación"
                    name="semanasGestacion"
                    type="number"
                    fullWidth
                    value={formik.values.semanasGestacion}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.semanasGestacion &&
                      Boolean(formik.errors.semanasGestacion)
                    }
                    helperText={
                      formik.touched.semanasGestacion &&
                      formik.errors.semanasGestacion
                    }
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
                    value={formik.values.grupoSanguineo}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.grupoSanguineo &&
                      Boolean(formik.errors.grupoSanguineo)
                    }
                    helperText={
                      formik.touched.grupoSanguineo &&
                      formik.errors.grupoSanguineo
                    }
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
                    value={formik.values.alergias}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.alergias && Boolean(formik.errors.alergias)
                    }
                    helperText={formik.touched.alergias && formik.errors.alergias}
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
                    value={formik.values.medicaciones}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.medicaciones &&
                      Boolean(formik.errors.medicaciones)
                    }
                    helperText={
                      formik.touched.medicaciones &&
                      formik.errors.medicaciones
                    }
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
                    value={formik.values.numeroSs}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.numeroSs && Boolean(formik.errors.numeroSs)}
                    helperText={formik.touched.numeroSs && formik.errors.numeroSs}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Pediatra"
                    name="pediatra"
                    fullWidth
                    value={formik.values.pediatra}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={formik.touched.pediatra && Boolean(formik.errors.pediatra)}
                    helperText={formik.touched.pediatra && formik.errors.pediatra}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Centro médico"
                    name="centroMedico"
                    fullWidth
                    value={formik.values.centroMedico}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.centroMedico &&
                      Boolean(formik.errors.centroMedico)
                    }
                    helperText={
                      formik.touched.centroMedico &&
                      formik.errors.centroMedico
                    }
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label="Número centro médico"
                    name="telefonoCentroMedico"
                    fullWidth
                    value={formik.values.telefonoCentroMedico}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                    error={
                      formik.touched.telefonoCentroMedico &&
                      Boolean(formik.errors.telefonoCentroMedico)
                    }
                    helperText={
                      formik.touched.telefonoCentroMedico &&
                      formik.errors.telefonoCentroMedico
                    }
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
                value={formik.values.observaciones}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                error={
                  formik.touched.observaciones &&
                  Boolean(formik.errors.observaciones)
                }
                helperText={
                  formik.touched.observaciones && formik.errors.observaciones
                }
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
                />
                <Button
                  variant="contained"
                  onClick={() => fileInputRef.current && fileInputRef.current.click()}
                >
                  Subir foto
                </Button>
              </Stack>
            </Box>
          </Grid>
        </Grid>

        <Stack direction="row" spacing={2} justifyContent="flex-end" sx={{ mt: 2 }}>
          <Button onClick={() => navigate(-1)}>Cancelar</Button>
          <Button type="submit" variant="contained">
            Guardar
          </Button>
        </Stack>
      </Box>
    </LocalizationProvider>
  );
}

