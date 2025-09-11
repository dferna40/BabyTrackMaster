import { render, screen, waitFor } from '@testing-library/react';
import RecentCareCard from './RecentCareCard';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { listarRecientes } from '../../services/cuidadosService';

jest.mock('../../services/cuidadosService', () => ({
  listarRecientes: jest.fn(),
}));

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
  },
}));

describe('RecentCareCard', () => {
  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  it('muestra tipoPanalNombre para cuidados de pañal', async () => {
    listarRecientes.mockResolvedValue({
      data: [
        {
          id: 1,
          tipoNombre: 'Pañal',
          inicio: new Date().toISOString(),
          tipoPanalNombre: 'Pipi',
        },
      ],
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <RecentCareCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarRecientes).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('Pipi')).toBeInTheDocument();
    });
  });

  it('muestra la duración proporcionada por la API para registros de sueño', async () => {
    const fin = new Date().toISOString();
    const inicio = new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString();
    listarRecientes.mockResolvedValue({
      data: [
        {
          id: 4,
          tipoNombre: 'Sueño',
          inicio,
          fin,
          duracion: '1h 30m',
        },
      ],
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <RecentCareCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarRecientes).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('1h 30m')).toBeInTheDocument();
    });
  });

  it('calcula la duración usando inicio y fin en UTC cuando no se recibe duración', async () => {
    const fin = new Date().toISOString();
    const inicio = new Date(Date.now() - 90 * 60 * 1000).toISOString();
    listarRecientes.mockResolvedValue({
      data: [
        {
          id: 2,
          tipoNombre: 'Sueño',
          inicio,
          fin,
          duracion: null,
        },
      ],
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <RecentCareCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarRecientes).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('1h 30m')).toBeInTheDocument();
    });
  });

  it('normaliza el tiempo al convertir desde UTC a la hora local', async () => {
    const originalTZ = process.env.TZ;
    process.env.TZ = 'America/Mexico_City';
    jest.useFakeTimers().setSystemTime(new Date('2024-07-22T06:30:00-05:00'));
    listarRecientes.mockResolvedValue({
      data: [
        {
          id: 3,
          tipoNombre: 'Pañal',
          inicio: '2024-07-22T10:00:00',
          tipoPanalNombre: 'Pipi',
        },
      ],
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <RecentCareCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarRecientes).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('Hace 1h 30m')).toBeInTheDocument();
    });

    process.env.TZ = originalTZ;
  });
});
