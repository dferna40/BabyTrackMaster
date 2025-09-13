import React, { useEffect, useState } from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";
import Stack from "@mui/material/Stack";
import FormControl from "@mui/material/FormControl";
import FormLabel from "@mui/material/FormLabel";
import dayjs from "dayjs";
import { DatePicker } from "@mui/x-date-pickers";
import { listarCategorias } from '../../services/gastosService';
import { saveButton, cancelButton } from '../../theme/buttonStyles';


export default function GastoForm({ open, onClose, onSubmit, initialData }) {
  const [formData, setFormData] = useState({
    fecha: null,
    categoriaId: "",
    descripcion: "",
    cantidad: "",
    categoriaNombre: "",
  });
  const [categorias, setCategorias] = useState([]);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (initialData) {
      setFormData({
        fecha: initialData.fecha ? dayjs(initialData.fecha) : null,
        categoriaId: initialData.categoriaId || "",
        descripcion: initialData.descripcion || "",
        cantidad: initialData.cantidad || "",
        categoriaNombre: initialData.categoriaNombre || "",
      });
    } else {
      setFormData({
        fecha: null,
        categoriaId: "",
        descripcion: "",
        cantidad: "",
        categoriaNombre: "",
      });
    }
    setErrors({});
  }, [initialData, open]);

  useEffect(() => {
    listarCategorias()
      .then((response) => {
        setCategorias(response.data);
      })
      .catch((error) => {
        console.error("Error fetching categorias:", error);
      });
  }, []);

  const selectedCategoriaNombre =
    categorias.find((option) => option.id === formData.categoriaId)?.nombre?.trim().toLowerCase() || "";
  const isCategoriaOtros = selectedCategoriaNombre === "otros";

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "cantidad") {
      const val = Number(value);
      setErrors((prev) => ({
        ...prev,
        cantidad: val >= 0 ? "" : "El precio no puede ser negativo",
      }));
    }
    if (name === "categoriaId") {
      const selectedName =
        categorias.find((option) => option.id === value)?.nombre?.trim().toLowerCase() || "";
      setFormData((prev) => ({
        ...prev,
        categoriaId: value,
        ...(selectedName !== "otros" ? { categoriaNombre: "" } : {}),
      }));
      if (selectedName !== "otros") {
        setErrors((prev) => ({ ...prev, categoriaNombre: "" }));
      }
      return;
    }
    if (name === "categoriaNombre") {
      setErrors((prev) => ({ ...prev, categoriaNombre: "" }));
    }
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (newValue) => {
    setFormData((prev) => ({ ...prev, fecha: newValue }));
    setErrors((prev) => ({ ...prev, fecha: "" }));
  };

  const handleSubmit = () => {
    const val = Number(formData.cantidad);
    if (val < 0) {
      setErrors((prev) => ({
        ...prev,
        cantidad: "El precio no puede ser negativo",
      }));
      return;
    }
    if (!formData.fecha) {
      setErrors((prev) => ({
        ...prev,
        fecha: "La fecha es obligatoria",
      }));
      return;
    }
    if (formData.fecha.isAfter(dayjs(), 'day')) {
      setErrors((prev) => ({
        ...prev,
        fecha: "La fecha no puede ser futura",
      }));
      return;
    }
    if (isCategoriaOtros && !formData.categoriaNombre) {
      setErrors((prev) => ({ ...prev, categoriaNombre: "Debe especificar la categoría" }));
      return;
    }
    const data = {
      ...formData,
      fecha: formData.fecha ? formData.fecha.format("YYYY-MM-DD") : "",
    };
    if (!isCategoriaOtros) {
      delete data.categoriaNombre;
    }
    onSubmit(data);
  };

  return (
    <Dialog open={open} onClose={onClose} fullWidth maxWidth="sm">
        <DialogTitle>
          {initialData && initialData.id ? 'Editar gasto' : 'Registrar gasto'}
        </DialogTitle>
        <DialogContent>
          <Stack sx={{ mt: 1 }}>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Fecha</FormLabel>
              <DatePicker
                value={formData.fecha}
                onChange={handleDateChange}
                disableFuture
                slotProps={{
                  textField: {
                    fullWidth: true,
                    required: true,
                    error: Boolean(errors.fecha),
                    helperText: errors.fecha,
                  },
                }}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Categoría</FormLabel>
              <TextField
                select
                name="categoriaId"
                value={formData.categoriaId}
                onChange={handleChange}
              >
                {categorias.map((option) => (
                  <MenuItem key={option.id} value={option.id}>
                    {option.nombre}
                  </MenuItem>
                ))}
              </TextField>
            </FormControl>
            {isCategoriaOtros && (
              <FormControl fullWidth sx={{ mb: 2 }}>
                <FormLabel sx={{ mb: 1 }}>Especificar</FormLabel>
                <TextField
                  name="categoriaNombre"
                  value={formData.categoriaNombre}
                  onChange={handleChange}
                  error={Boolean(errors.categoriaNombre)}
                  helperText={errors.categoriaNombre}
                />
              </FormControl>
            )}
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Precio (€)</FormLabel>
              <TextField
                type="number"
                name="cantidad"
                value={formData.cantidad}
                onChange={handleChange}
                inputProps={{ min: 0 }}
                error={Boolean(errors.cantidad)}
                helperText={errors.cantidad}
              />
            </FormControl>
            <FormControl fullWidth sx={{ mb: 2 }}>
              <FormLabel sx={{ mb: 1 }}>Descripción</FormLabel>
              <TextField
                multiline
                rows={3}
                name="descripcion"
                value={formData.descripcion}
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
