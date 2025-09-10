import { render, screen } from '@testing-library/react';
import dayjs from 'dayjs';

jest.mock(
  'react-router-dom',
  () => ({
    __esModule: true,
    useNavigate: () => jest.fn(),
  }),
  { virtual: true }
);

const UpcomingAppointmentsCard = require('./UpcomingAppointmentsCard').default;

describe('UpcomingAppointmentsCard', () => {
  it('no muestra citas pasadas', () => {
    const past = dayjs().subtract(1, 'day');
    const future = dayjs().add(1, 'day');
    const appointments = [
      {
        id: 1,
        fecha: past.format('YYYY-MM-DD'),
        hora: past.format('HH:mm'),
        motivo: 'Pasada',
        tipoNombre: 'consulta',
      },
      {
        id: 2,
        fecha: future.format('YYYY-MM-DD'),
        hora: future.format('HH:mm'),
        motivo: 'Futura',
        tipoNombre: 'vacuna',
      },
    ];

    render(<UpcomingAppointmentsCard appointments={appointments} />);

    expect(screen.queryByText('Pasada')).not.toBeInTheDocument();
    expect(screen.getByText('Futura')).toBeInTheDocument();
  });

  it('muestra mensaje cuando todas las citas son pasadas', () => {
    const past = dayjs().subtract(1, 'day');
    const appointments = [
      {
        id: 1,
        fecha: past.format('YYYY-MM-DD'),
        hora: past.format('HH:mm'),
        motivo: 'Pasada',
        tipoNombre: 'consulta',
      },
    ];

    render(<UpcomingAppointmentsCard appointments={appointments} />);

    expect(screen.getByText('No hay citas próximas.')).toBeInTheDocument();
  });
});

