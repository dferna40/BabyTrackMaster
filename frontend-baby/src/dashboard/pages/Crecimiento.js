
import React, { useEffect, useState, useMemo } from "react";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import IconButton from "@mui/material/IconButton";
import Stack from "@mui/material/Stack";
import Tab from "@mui/material/Tab";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TablePagination from "@mui/material/TablePagination";
import Tabs from "@mui/material/Tabs";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import dayjs from "dayjs";
import AddIcon from "@mui/icons-material/Add";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import { LineChart } from "@mui/x-charts/LineChart";
import {
  listarPorBebe,
  crearRegistro,
  actualizarRegistro,
  eliminarRegistro,
  listarTipos,
} from "../../services/crecimientoService";
import CrecimientoForm from "../components/CrecimientoForm";
import { BabyContext } from "../../context/BabyContext";
import { AuthContext } from "../../context/AuthContext";
import { addButton } from "../../theme/buttonStyles";

function getLabelsByTipo(tipo) {
  switch (tipo?.nombre) {
    case "Peso":
      return {
        valorHeader: "Peso (kg)",
        chartTitle: "Evolución del peso",
        seriesLabel: "Peso",
      };
    case "Talla":
      return {
        valorHeader: "Medida (cm)",
        chartTitle: "Evolución de la talla",
        seriesLabel: "Talla",
      };
    case "Perímetro cefálico":
      return {
        valorHeader: "Medida (cm)",
        chartTitle: "Evolución del perímetro cefálico",
        seriesLabel: "Perímetro cefálico",
      };
    default:
      return {
        valorHeader: "Valor",
        chartTitle: "Evolución",
      };
  }
}

export default function Crecimiento() {
  const [tab, setTab] = useState(0);
  const [registros, setRegistros] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openForm, setOpenForm] = useState(false);
  const [selectedRegistro, setSelectedRegistro] = useState(null);
  const [tipos, setTipos] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const bebeId = activeBaby?.id;
  const usuarioId = user?.id;
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const labels = useMemo(() => getLabelsByTipo(tipos[tab]), [tipos, tab]);

  const filteredRegistros = useMemo(() => {
    let data =
      tipos[tab]
        ? registros.filter(
            (r) => Number(r.tipoId) === Number(tipos[tab].id),
          )
        : [];
    if (startDate) {
      data = data.filter((r) =>
        dayjs(r.fecha).isAfter(dayjs(startDate).subtract(1, "day")),
      );
    }
    if (endDate) {
      data = data.filter((r) =>
        dayjs(r.fecha).isBefore(dayjs(endDate).add(1, "day")),
      );
    }
    return data;
  }, [registros, tab, tipos, startDate, endDate]);

  const chartData = filteredRegistros.map((r) => ({
    x: dayjs(r.fecha).format("DD/MM"),
    y: r.valor,
    p: r.percentil,
  }));

  const fetchRegistros = () => {
    if (!bebeId || !usuarioId) return;
    listarPorBebe(usuarioId, bebeId)
      .then((res) => setRegistros(res.data))
      .catch((err) => console.error("Error fetching crecimiento:", err));
  };

  useEffect(() => {
    if (bebeId) {
      fetchRegistros();
      listarTipos()
        .then((res) => setTipos(res.data))
        .catch((err) =>
          console.error("Error fetching tipos crecimiento:", err),
        );
    }
  }, [bebeId]);

  const handleTabChange = (event, newValue) => {
    setTab(newValue);
    setPage(0);
  };

  const handleAdd = () => {
    setSelectedRegistro(null);
    setOpenForm(true);
  };

  const handleEdit = (registro) => {
    setSelectedRegistro(registro);
    setOpenForm(true);
  };

  const handleDelete = (id) => {
    if (!bebeId || !usuarioId) return;
    if (window.confirm("¿Eliminar registro?")) {
      eliminarRegistro(usuarioId, id)
        .then(() => fetchRegistros())
        .catch((err) => console.error("Error deleting registro:", err));
    }
  };

  const handleFormSubmit = (data) => {
    if (!bebeId || !usuarioId) return;
    const payload = { ...data, bebeId };
    const request = selectedRegistro
      ? actualizarRegistro(usuarioId, selectedRegistro.id, payload)
      : crearRegistro(usuarioId, payload);

    request
      .then(() => {
        setOpenForm(false);
        setSelectedRegistro(null);
        fetchRegistros();
      })
      .catch((err) => console.error("Error saving registro:", err));
  };

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Box sx={{ width: "100%" }}>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        justifyContent="flex-start"
        alignItems={{ xs: "stretch", sm: "center" }}
        spacing={2}
        mb={2}
      >
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          sx={{ alignSelf: "flex-start", ...addButton }}
          onClick={handleAdd}
        >
          Registrar crecimiento
        </Button>
        <TextField
          label="Desde"
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          label="Hasta"
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
      </Stack>

      <Typography variant="h4" gutterBottom>
        Crecimiento
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        {tipos.map((t) => (
          <Tab key={t.id} label={t.nombre} />
        ))}
      </Tabs>
      <TableContainer sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Fecha</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>{labels.valorHeader}</TableCell>
              <TableCell>Observaciones</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filteredRegistros
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((registro) => (
                <TableRow key={registro.id}>
                  <TableCell>
                    {dayjs(registro.fecha).format("DD/MM/YYYY")}
                  </TableCell>
                  <TableCell>{registro.tipoNombre}</TableCell>
                  <TableCell sx={{ fontWeight: 600 }}>
                    {registro.valor} {registro.unidad || ""}
                  </TableCell>
                  <TableCell>{registro.observaciones}</TableCell>
                  <TableCell align="center">
                    <IconButton
                      size="small"
                      aria-label="edit"
                      onClick={() => handleEdit(registro)}
                      sx={{ color: "#0d6efd" }}
                    >
                      <EditIcon fontSize="small" />
                    </IconButton>
                    <IconButton
                      size="small"
                      aria-label="delete"
                      onClick={() => handleDelete(registro.id)}
                      sx={{ color: "#dc3545" }}
                    >
                      <DeleteIcon fontSize="small" />
                    </IconButton>
                  </TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {filteredRegistros
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map((registro) => (
                    <TableRow key={registro.id}>
                      <TableCell>
                        {dayjs(registro.fecha).format("DD/MM/YYYY")}
                      </TableCell>
                      <TableCell>{registro.tipoNombre}</TableCell>
                      <TableCell sx={{ fontWeight: 600 }}>
                        {registro.valor} {registro.unidad || ""}
                      </TableCell>
                      <TableCell>{registro.observaciones}</TableCell>
                      <TableCell align="center">
                        <IconButton
                          size="small"
                          aria-label="edit"
                          onClick={() => handleEdit(registro)}
                          sx={{ color: "#0d6efd" }}
                        >
                          <EditIcon fontSize="small" />
                        </IconButton>
                        <IconButton
                          size="small"
                          aria-label="delete"
                          onClick={() => handleDelete(registro.id)}
                          sx={{ color: "#dc3545" }}
                        >
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
              </TableBody>
            </Table>
            <TablePagination
              component="div"
              count={filteredRegistros.length}
              page={page}
              onPageChange={handleChangePage}
              rowsPerPage={rowsPerPage}
              onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </TableContainer>
          <Card variant="outlined">
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Evolución
              </Typography>
              <LineChart
                height={300}
                xAxis={[
                  {
                    scaleType: "point",
                    data: chartData.map((d) => d.x),
                  },
                ]}
                series={[
                  { data: chartData.map((d) => d.y), label: "Valor" },
                  ...(chartData.some((d) => d.p !== undefined)
                    ? [{ data: chartData.map((d) => d.p), label: "Percentil OMS" }]
                    : []),
                ]}
                margin={{ left: 40, right: 20, top: 20, bottom: 20 }}
                grid={{ horizontal: true }}
              />
            </CardContent>
          </Card>
        </>
      ) : (
        tipos[tab] && (
          <Typography>
            Aún no se han insertado datos de {tipos[tab].nombre.toLowerCase()}
          </Typography>
        )
      )}
      <CrecimientoForm
        open={openForm}
        onClose={() => setOpenForm(false)}
        onSubmit={handleFormSubmit}
        initialData={selectedRegistro}
      />
    </Box>
  );
}
