import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import Radio from '@mui/material/Radio';
import Switch from '@mui/material/Switch';
import Avatar from '@mui/material/Avatar';
import Stack from '@mui/material/Stack';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { crearBebe } from '../../services/bebesService';

export default function AnadirBebe() {
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const [preview, setPreview] = useState(null);
  const [formData, setFormData] = useState({
    nombre: '',
    fechaNacimiento: null,
    sexo: 'ND',
    activo: true,
    pesoNacimiento: '',
    tallaNacimiento: '',
    semanasGestacion: '',
    color: '#000000',
    emoji: '',
    foto: null,
  });

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
      setFormData((prev) => ({ ...prev, foto: file }));
      setPreview(URL.createObjectURL(file));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = new FormData();
    Object.entries(formData).forEach(([key, value]) => {
      if (value !== null && value !== '') {
        if (dayjs.isDayjs(value)) {
          payload.append(key, value.format('YYYY-MM-DD'));
        } else {
          payload.append(key, value);
        }
      }
    });

    crearBebe(payload)
      .then(() => navigate(-1))
      .catch((error) => {
        console.error('Error creating baby:', error);
      });
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box component="form" onSubmit={handleSubmit} sx={{ flexGrow: 1 }}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              required
              label="Nombre del bebé"
              name="nombre"
              fullWidth
              value={formData.nombre}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <DatePicker
              label="Fecha de nacimiento"
              value={formData.fechaNacimiento}
              onChange={handleDateChange}
              slotProps={{ textField: { fullWidth: true } }}
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
                  checked={formData.activo}
                  onChange={handleChange}
                  name="activo"
                />
              }
              label="Bebé activo"
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Peso al nacer (kg)"
              name="pesoNacimiento"
              type="number"
              fullWidth
              value={formData.pesoNacimiento}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Talla al nacer (cm)"
              name="tallaNacimiento"
              type="number"
              fullWidth
              value={formData.tallaNacimiento}
              onChange={handleChange}
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
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Color"
              name="color"
              type="color"
              fullWidth
              value={formData.color}
              onChange={handleChange}
              InputLabelProps={{ shrink: true }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              label="Emoji"
              name="emoji"
              fullWidth
              value={formData.emoji}
              onChange={handleChange}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <Stack direction="row" spacing={2} alignItems="center">
              <Avatar src={preview} sx={{ width: 56, height: 56 }} />
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
          </Grid>
          <Grid item xs={12}>
            <Stack direction="row" spacing={2} justifyContent="flex-end">
              <Button onClick={() => navigate(-1)}>Cancelar</Button>
              <Button type="submit" variant="contained">
                Guardar
              </Button>
            </Stack>
          </Grid>
        </Grid>
      </Box>
    </LocalizationProvider>
  );
}

