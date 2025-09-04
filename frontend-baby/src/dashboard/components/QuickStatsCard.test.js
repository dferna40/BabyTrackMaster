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
    obtenerStatsRapidas.mockResolvedValue({
      data: { horasSueno: 8, panales: 5, tomas: 6, banos: 1 },
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <QuickStatsCard />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    expect(obtenerStatsRapidas).toHaveBeenCalledWith(1, 2);

    await waitFor(() => {
      expect(screen.getByText('8h')).toBeInTheDocument();
      expect(screen.getByText('5')).toBeInTheDocument();
      expect(screen.getByText('6')).toBeInTheDocument();
      expect(screen.getByText('1')).toBeInTheDocument();
    });
  });
});
