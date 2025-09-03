import React, { useEffect, useState } from 'react';
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
import { BabyContext } from '../../context/BabyContext';
import { listar } from '../../services/citasService';

export default function UpcomingAppointmentsCard() {
  const [appointments, setAppointments] = useState([]);
  const { activeBaby } = React.useContext(BabyContext);

  useEffect(() => {
    if (!activeBaby?.id) return;
    listar(activeBaby.id, 0, 100)
      .then((response) => {
        const items = Array.isArray(response.data)
          ? response.data
          : response.data?.content;
        if (Array.isArray(items)) {
          const upcoming = items
            .map((c) => ({
              ...c,
              tipoNombre: c.tipo?.nombre ?? c.tipoNombre,
              estadoNombre: c.estado?.nombre ?? c.estadoNombre,
            }))
            .filter((c) => dayjs(`${c.fecha}T${c.hora}`) >= dayjs())
            .sort((a, b) =>
              dayjs(`${a.fecha}T${a.hora}`).diff(dayjs(`${b.fecha}T${b.hora}`))
            )
            .slice(0, 10);
          setAppointments(upcoming);
        } else {
          setAppointments([]);
        }
      })
      .catch((err) => {
        console.error('Error fetching citas:', err);
        setAppointments([]);
      });
  }, [activeBaby]);

  return (
    <Card variant="outlined" sx={{ height: '100%' }}>
      <CardContent>
        <Typography variant="h6" component="h2" gutterBottom>
          Próximas Citas
        </Typography>
        {appointments.length > 0 ? (
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell>Fecha</TableCell>
                <TableCell>Hora</TableCell>
                <TableCell>Motivo</TableCell>
                <TableCell>Tipo</TableCell>
                <TableCell>Estado</TableCell>
                <TableCell>Centro médico</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {appointments.map((c) => (
                <TableRow key={c.id}>
                  <TableCell>
                    {dayjs(c.fecha).locale('es').format('DD/MM/YYYY')}
                  </TableCell>
                  <TableCell>
                    {dayjs(`${c.fecha}T${c.hora}`).locale('es').format('HH:mm')}
                  </TableCell>
                  <TableCell>{c.motivo}</TableCell>
                  <TableCell>{c.tipoNombre}</TableCell>
                  <TableCell>{c.estadoNombre}</TableCell>
                  <TableCell>{c.centroMedico}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        ) : (
          <Typography variant="body2" color="text.secondary">
            No hay citas próximas.
          </Typography>
        )}
      </CardContent>
    </Card>
  );
}
