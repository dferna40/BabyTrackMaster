import React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import MenuItem from '@mui/material/MenuItem';
import Paper from '@mui/material/Paper';
import Select from '@mui/material/Select';
import Stack from '@mui/material/Stack';
import Tab from '@mui/material/Tab';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Tabs from '@mui/material/Tabs';
import TextField from '@mui/material/TextField';
import Typography from '@mui/material/Typography';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import SearchIcon from '@mui/icons-material/Search';
import { BarChart } from '@mui/x-charts/BarChart';

const rows = [
  { time: '07:15', type: 'Biberón', qty: '120 ml', note: 'Comió bien' },
  { time: '09:05', type: 'Pañal', qty: 'Sucio', note: 'Parece tener hambre' },
  { time: '12:30', type: 'Biberón', qty: '150 ml', note: 'Un poco intranquilo' },
  { time: '15:00', type: 'Sueño', qty: '1 h 30 m', note: 'Durmió profundamente' },
  { time: '19:15', type: 'Baño', qty: '-', note: 'Parecía relajado' },
];

export default function Cuidados() {
  const [tab, setTab] = React.useState(0);

  const handleTabChange = (event, newValue) => {
    setTab(newValue);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <Stack
        direction={{ xs: 'column', sm: 'row' }}
        justifyContent="space-between"
        alignItems={{ xs: 'stretch', sm: 'center' }}
        spacing={2}
        mb={2}
      >
        <Button variant="contained" startIcon={<AddIcon />} sx={{ alignSelf: 'flex-start' }}>
          Añadir nuevo cuidado
        </Button>
        <Stack direction="row" spacing={2} flexWrap="wrap">
          <TextField
            size="small"
            placeholder="Buscar"
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon fontSize="small" />
                </InputAdornment>
              ),
            }}
          />
          <Select size="small" defaultValue="Todos">
            <MenuItem value="Todos">Todos</MenuItem>
            <MenuItem value="Biberón">Biberón</MenuItem>
            <MenuItem value="Pañal">Pañal</MenuItem>
            <MenuItem value="Sueño">Sueño</MenuItem>
            <MenuItem value="Baño">Baño</MenuItem>
          </Select>
          <TextField type="date" size="small" />
        </Stack>
      </Stack>

      <Typography variant="h4" gutterBottom>
        Cuidados
      </Typography>
      <Tabs value={tab} onChange={handleTabChange} sx={{ mb: 2 }}>
        <Tab label="Biberón" />
        <Tab label="Pañal" />
        <Tab label="Sueño" />
        <Tab label="Baño" />
      </Tabs>

      <TableContainer component={Paper} sx={{ mb: 4 }}>
        <Table size="small">
          <TableHead>
            <TableRow>
              <TableCell>Hora</TableCell>
              <TableCell>Tipo</TableCell>
              <TableCell>Cantidad</TableCell>
              <TableCell>Nota</TableCell>
              <TableCell align="center">Acciones</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {rows.map((row, index) => (
              <TableRow key={index}>
                <TableCell>{row.time}</TableCell>
                <TableCell>{row.type}</TableCell>
                <TableCell>{row.qty}</TableCell>
                <TableCell>{row.note}</TableCell>
                <TableCell align="center">
                  <IconButton size="small" aria-label="edit">
                    <EditIcon fontSize="small" />
                  </IconButton>
                  <IconButton size="small" aria-label="delete">
                    <DeleteIcon fontSize="small" />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Card variant="outlined">
        <CardContent>
          <Typography variant="h6" gutterBottom>
            Estadísticas de cuidados
          </Typography>
          <BarChart
            height={250}
            xAxis={[{ scaleType: 'band', data: ['Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb', 'Dom'] }]}
            series={[{ data: [4, 3, 3, 2, 4, 2, 1] }]}
            margin={{ left: 30, right: 10, top: 20, bottom: 20 }}
            grid={{ horizontal: true }}
          />
        </CardContent>
      </Card>
    </Box>
  );
}
