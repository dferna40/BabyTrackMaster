import React, { useEffect, useState } from 'react';
import { BabyContext } from '../../context/BabyContext';
import { AuthContext } from '../../context/AuthContext';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import dayjs from 'dayjs';
import 'dayjs/locale/es';
import { listarRecientes } from '../../services/cuidadosService';

export default function RecentCareCard() {
  const [recentCare, setRecentCare] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);
  const { user } = React.useContext(AuthContext);
  const usuarioId = user?.id;
  const bebeId = activeBaby?.id;

  useEffect(() => {
    if (bebeId && usuarioId) {
      listarRecientes(usuarioId, bebeId)
        .then((response) => setRecentCare(response.data))
        .catch((error) => console.error('Error fetching recent care:', error));
    }
  }, [bebeId, usuarioId]);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Cuidados Recientes
        </Typography>
        {recentCare.length > 0 ? (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Fecha</TableCell>
                <TableCell>Hora</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell>Cantidad (ml)</TableCell>
                <TableCell>Observaciones</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {recentCare.slice(0, 5).map((item) => (
                <TableRow key={item.id}>
                  <TableCell>{dayjs(item.inicio).locale('es').format('DD/MM/YYYY')}</TableCell>
                  <TableCell>{dayjs(item.inicio).locale('es').format('HH:mm')}</TableCell>
                  <TableCell>{item.tipoNombre}</TableCell>
                  <TableCell>{item.cantidadMl ?? '-'}</TableCell>
                  <TableCell>{item.observaciones ?? ''}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No hay cuidados recientes.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
}
