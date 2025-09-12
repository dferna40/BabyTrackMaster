import { render, screen, waitFor } from '@testing-library/react';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import QuickActionsCard from './QuickActionsCard';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { listarRecientes as listarAlimentacionRecientes } from '../../services/alimentacionService';
import { listarRecientes as listarCuidadosRecientes, obtenerStatsRapidas } from '../../services/cuidadosService';
import { listarRecientes as listarGastosRecientes } from '../../services/gastosService';

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  },
}));

jest.mock(
  'react-router-dom',
  () => ({
    __esModule: true,
    useNavigate: () => jest.fn(),
  }),
  { virtual: true }
);

jest.mock('../../services/alimentacionService', () => ({
  listarRecientes: jest.fn(),
}));

jest.mock('../../services/cuidadosService', () => ({
  listarRecientes: jest.fn(),
  obtenerStatsRapidas: jest.fn(),
}));

jest.mock('../../services/gastosService', () => ({
  listarRecientes: jest.fn(),
}));

dayjs.extend(relativeTime);

if (typeof global.structuredClone !== 'function') {
  global.structuredClone = (val) => JSON.parse(JSON.stringify(val));
}

describe('QuickActionsCard', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra total de mililitros de biberón consumidos hoy', async () => {
    const now = dayjs();
    listarAlimentacionRecientes.mockResolvedValue({
      data: [
        {
          fechaHora: now.toISOString(),
          tipoAlimentacion: { nombre: 'Biberón' },
          cantidadMl: 100,
        },
        {
          fechaHora: now.toISOString(),
          tipoAlimentacion: { nombre: 'Biberón' },
          cantidadMl: 50,
        },
        {
          fechaHora: now.subtract(1, 'day').toISOString(),
          tipoAlimentacion: { nombre: 'Biberón' },
          cantidadMl: 200,
        },
      ],
    });
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarCuidadosRecientes.mockResolvedValue({ data: [] });
    listarGastosRecientes.mockResolvedValue([]);

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <QuickActionsCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => {
      expect(listarAlimentacionRecientes).toHaveBeenCalledWith(1, 2, 20);
    });

    await waitFor(() => {
      expect(screen.getByText('Hoy: 150ml')).toBeInTheDocument();
    });
  });

  it('suma correctamente los baños del día de hoy', async () => {
    const now = dayjs();
    listarAlimentacionRecientes.mockResolvedValue({ data: [] });
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarCuidadosRecientes
      .mockResolvedValueOnce({ data: [] })
      .mockResolvedValueOnce({
        data: [
          {
            tipoNombre: 'Baño',
            inicio: now.toISOString(),
            cantidadMl: 1,
          },
          {
            tipoNombre: 'Baño',
            inicio: now.toISOString(),
            cantidadMl: 2,
          },
          {
            tipoNombre: 'Baño',
            inicio: now.subtract(1, 'day').toISOString(),
            cantidadMl: 5,
          },
        ],
      });
    listarGastosRecientes.mockResolvedValue([]);

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <QuickActionsCard />
        </BabyContext.Provider>
      </AuthContext.Provider>,
    );

    await waitFor(() => {
      expect(screen.getByText('Hoy: 3')).toBeInTheDocument();
    });
  });
});

