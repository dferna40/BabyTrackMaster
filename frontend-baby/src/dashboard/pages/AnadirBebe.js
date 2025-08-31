import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
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
    colorEmoji: false,
    color: '#9c27b0',
    emoji: '😊',
    foto: null,
  });

  const [emojiAnchorEl, setEmojiAnchorEl] = useState(null);
  const emojiOptions = ['😊', '😀', '😍', '😎', '😢', '😡', '😴', '🤗', '🥳', '👶'];

  const handleEmojiClick = (event) => {    setEmojiAnchorEl(event.currentTarget);  };

  const handleEmojiSelect = (emoji) => {
    setFormData((prev) => ({ ...prev, emoji }));
    setEmojiAnchorEl(null);
  };

  const handleEmojiClose = () => {
    setEmojiAnchorEl(null);
  };

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
      if (key === 'colorEmoji') return;
      if ((key === 'color' || key === 'emoji') && !formData.colorEmoji) return;
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
              </Grid>
            </Box>

            <Box component={Paper} sx={{ p: 2, mb: 2 }}>
              <Typography variant="h6" gutterBottom>
                Preferencias
              </Typography>
              <Stack direction="row" alignItems="center" spacing={2} justifyContent="space-between">
                <Typography>Color/emoji identificador</Typography>
                <Stack direction="row" spacing={1} alignItems="center">
                  <Switch
                    name="colorEmoji"
                    checked={formData.colorEmoji}
                    onChange={handleChange}
                    InputLabelProps={{ shrink: true }}
                  />
                  {formData.colorEmoji && (
                    <>
                      <Box
                        component="input"
                        type="color"
                        name="color"
                        value={formData.color}
                        onChange={handleChange}
                        sx={{ width: 40, height: 40, p: 0, border: 'none', bgcolor: 'transparent' }}
                      />
                      <TextField
                        variant="standard"
                        name="emoji"
                        value={formData.emoji}
                        onClick={handleEmojiClick}
                        inputProps={{ readOnly: true, maxLength: 2, style: { fontSize: '1.5rem', textAlign: 'center', cursor: 'pointer' } }}
                        sx={{ width: 48 }}
                      />
                      <Menu
                        anchorEl={emojiAnchorEl}
                        open={Boolean(emojiAnchorEl)}
                        onClose={handleEmojiClose}
                         >
                          {emojiOptions.map((em) => (
                           <MenuItem key={em} onClick={() => handleEmojiSelect(em)}>
                            {em}
                            </MenuItem>
                            ))}
                         </Menu>
                    </>
                  )}
                </Stack>
              </Stack>
            </Box>
            <Button variant="outlined" sx={{ mb: 2 }}>
              Datos adicionales
            </Button>
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

