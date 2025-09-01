import React from 'react';
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
  const handleSave = () => {
    const hasEmptyFields = Object.values(formData).some(
      (value) => value === undefined || value === null || String(value).trim() === ''
    );

    if (hasEmptyFields) {
      alert('Por favor, completa todos los campos.');
      return;
    }

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
              value={formData.numeroSs || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Grupo sanguíneo</FormLabel>
            <TextField
              name="grupoSanguineo"
              value={formData.grupoSanguineo || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Medicaciones</FormLabel>
            <TextField
              multiline
              rows={3}
              name="medicaciones"
              value={formData.medicaciones || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Alergias</FormLabel>
            <TextField
              multiline
              rows={3}
              name="alergias"
              value={formData.alergias || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Pediatra</FormLabel>
            <TextField
              name="pediatra"
              value={formData.pediatra || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Centro médico</FormLabel>
            <TextField
              name="centroMedico"
              value={formData.centroMedico || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Teléfono centro médico</FormLabel>
            <TextField
              name="telefonoCentroMedico"
              value={formData.telefonoCentroMedico || ''}
              onChange={onChange}
            />
          </FormControl>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <FormLabel sx={{ mb: 1 }}>Observaciones</FormLabel>
            <TextField
              multiline
              rows={3}
              name="observaciones"
              value={formData.observaciones || ''}
              onChange={onChange}
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
