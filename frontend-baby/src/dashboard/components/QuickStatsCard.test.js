import { render, screen, waitFor } from '@testing-library/react';
import QuickStatsCard from './QuickStatsCard';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { obtenerStatsRapidas } from '../../services/cuidadosService';

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
  },
}));

jest.mock('../../services/cuidadosService', () => ({
  obtenerStatsRapidas: jest.fn(),
}));

if (typeof global.structuredClone !== 'function') {
  global.structuredClone = (val) => JSON.parse(JSON.stringify(val));
}

describe('QuickStatsCard', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra estadísticas obtenidas del servicio', async () => {
    obtenerStatsRapidas
      .mockResolvedValueOnce({
        data: { horasSueno: '8h', panales: 5, banos: 1 },
      })
      .mockResolvedValueOnce({
        data: { horasSueno: '7h', panales: 4, banos: 1 },
      });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <QuickStatsCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    expect(obtenerStatsRapidas).toHaveBeenNthCalledWith(1, 1, 2);
    expect(obtenerStatsRapidas).toHaveBeenNthCalledWith(
      2,
      1,
      2,
      expect.any(Number)
    );

    await waitFor(() => {
      expect(screen.getByText('8h')).toBeInTheDocument();
      expect(screen.getByText('1 más que ayer')).toBeInTheDocument();
      expect(screen.getByText('5')).toBeInTheDocument();
      expect(screen.getAllByText('1').length).toBeGreaterThan(0);
    });
  });

  it('muestra mensaje cuando el servicio devuelve valores por defecto', async () => {
    obtenerStatsRapidas.mockResolvedValue({
      data: { horasSueno: '0h', panales: 0, banos: 0 },
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <QuickStatsCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    expect(obtenerStatsRapidas).toHaveBeenNthCalledWith(1, 1, 2);
    expect(obtenerStatsRapidas).toHaveBeenNthCalledWith(
      2,
      1,
      2,
      expect.any(Number)
    );

    await waitFor(() => {
      expect(
        screen.getByText('No hay estadísticas que mostrar para el día de hoy.')
      ).toBeInTheDocument();
    });
  });
});
