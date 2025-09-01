import React, { useEffect, useState } from 'react';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Stack from '@mui/material/Stack';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';

export default function DatosAdicionalesForm({ open, onClose, formData, onChange }) {
  const [localData, setLocalData] = useState({});
  const [initialData, setInitialData] = useState({});
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (open) {
      const current = {
        numeroSs: formData.numeroSs || '',
        grupoSanguineo: formData.grupoSanguineo || '',
        medicaciones: formData.medicaciones || '',
        alergias: formData.alergias || '',
        pediatra: formData.pediatra || '',
        centroMedico: formData.centroMedico || '',
        telefonoCentroMedico: formData.telefonoCentroMedico || '',
        observaciones: formData.observaciones || '',
      };
      setLocalData(current);
      setInitialData(current);
      setErrors({});
    }
  }, [open, formData]);

  const handleFieldChange = (e) => {
    const { name, value } = e.target;
    setLocalData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSave = () => {
    const newErrors = {};
    if (localData.numeroSs && !/^\d+$/.test(localData.numeroSs)) {
      newErrors.numeroSs = 'Debe contener solo números';
    }
    if (
      localData.telefonoCentroMedico &&
      !/^\d+$/.test(localData.telefonoCentroMedico)
    ) {
      newErrors.telefonoCentroMedico = 'Debe contener solo números';
    }
    setErrors(newErrors);
    if (Object.keys(newErrors).length > 0) return;

    Object.keys(localData).forEach((key) => {
      if (localData[key] !== initialData[key]) {
        onChange({ target: { name: key, value: localData[key] } });
      }
    });
    onClose();
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
      <DialogTitle>Datos adicionales</DialogTitle>
      <DialogContent>
        <Stack sx={{ mt: 1 }}>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Número SS</FormLabel>
            <TextField
              name="numeroSs"
              value={localData.numeroSs || ''}
              onChange={handleFieldChange}
              error={Boolean(errors.numeroSs)}
              helperText={errors.numeroSs}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Grupo sanguíneo</FormLabel>
            <TextField
              name="grupoSanguineo"
              value={localData.grupoSanguineo || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Medicaciones</FormLabel>
            <TextField
              multiline
              rows={3}
              name="medicaciones"
              value={localData.medicaciones || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Alergias</FormLabel>
            <TextField
              multiline
              rows={3}
              name="alergias"
              value={localData.alergias || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Pediatra</FormLabel>
            <TextField
              name="pediatra"
              value={localData.pediatra || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Centro médico</FormLabel>
            <TextField
              name="centroMedico"
              value={localData.centroMedico || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Teléfono centro médico</FormLabel>
            <TextField
              name="telefonoCentroMedico"
              value={localData.telefonoCentroMedico || ''}
              onChange={handleFieldChange}
              error={Boolean(errors.telefonoCentroMedico)}
              helperText={errors.telefonoCentroMedico}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Observaciones</FormLabel>
            <TextField
              multiline
              rows={3}
              name="observaciones"
              value={localData.observaciones || ''}
              onChange={handleFieldChange}
            />
          </FormControl>
        </Stack>
      </DialogContent>
      <DialogActions>
        <Button onClick={onClose}>Cancelar</Button>
        <Button onClick={handleSave} variant="contained">Guardar</Button>
      </DialogActions>
    </Dialog>
  );
}
