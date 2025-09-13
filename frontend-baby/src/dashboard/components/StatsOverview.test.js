import { render, screen, waitFor } from '@testing-library/react';
import StatsOverview from './StatsOverview';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { obtenerStatsRapidas } from '../../services/cuidadosService';
import { obtenerUltimoBiberon } from '../../services/alimentacionService';
import { listarTipos, listarUltimosPorTipo } from '../../services/crecimientoService';

jest.mock('../../services/cuidadosService', () => ({
  obtenerStatsRapidas: jest.fn(),
}));

jest.mock('../../services/alimentacionService', () => ({
  obtenerUltimoBiberon: jest.fn(),
}));

jest.mock('../../services/crecimientoService', () => ({
  listarTipos: jest.fn(),
  listarUltimosPorTipo: jest.fn(),
}));

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
  },
}));

describe('StatsOverview', () => {
  afterEach(() => {
    jest.clearAllMocks();
    jest.useRealTimers();
  });

  const renderComponent = () =>
    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 2 } }}>
          <StatsOverview />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

  it('muestra el tiempo transcurrido desde el último biberón', async () => {
    const now = new Date('2024-01-01T12:00:00Z');
    jest.useFakeTimers().setSystemTime(now);
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarTipos.mockResolvedValue({ data: [] });
    const lastDate = new Date(
      now.getTime() - (2 * 60 + 15) * 60 * 1000
    ).toISOString();
    obtenerUltimoBiberon.mockResolvedValue({
      data: { fechaHora: lastDate },
    });

    renderComponent();

    await waitFor(() => expect(obtenerUltimoBiberon).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('Hace 2h 15m')).toBeInTheDocument();
    });
  });

  it('muestra "Sin datos" cuando no hay registros', async () => {
    jest.useFakeTimers().setSystemTime(new Date('2024-01-01T12:00:00Z'));
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarTipos.mockResolvedValue({ data: [] });
    obtenerUltimoBiberon.mockResolvedValue({ data: {} });

    renderComponent();

    await waitFor(() => expect(obtenerUltimoBiberon).toHaveBeenCalled());
    await waitFor(() => {
      expect(screen.getByText('Sin datos')).toBeInTheDocument();
    });
  });

  it('muestra flecha hacia arriba cuando el peso aumenta', async () => {
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarTipos.mockResolvedValue({ data: [{ id: 1, nombre: 'Peso' }] });
    listarUltimosPorTipo.mockResolvedValue({
      data: [
        { valor: 5.5 },
        { valor: 4.0 },
      ],
    });
    obtenerUltimoBiberon.mockResolvedValue({ data: {} });

    renderComponent();

    await waitFor(() => expect(listarUltimosPorTipo).toHaveBeenCalled());
    const icon = await screen.findByTestId('TrendingUpIcon');
    expect(icon).toHaveClass('MuiSvgIcon-colorSuccess');
  });

  it('muestra flecha hacia abajo cuando el peso disminuye', async () => {
    obtenerStatsRapidas.mockResolvedValue({ data: {} });
    listarTipos.mockResolvedValue({ data: [{ id: 1, nombre: 'Peso' }] });
    listarUltimosPorTipo.mockResolvedValue({
      data: [
        { valor: 4.0 },
        { valor: 5.5 },
      ],
    });
    obtenerUltimoBiberon.mockResolvedValue({ data: {} });

    renderComponent();

    await waitFor(() => expect(listarUltimosPorTipo).toHaveBeenCalled());
    const icon = await screen.findByTestId('TrendingDownIcon');
    expect(icon).toHaveClass('MuiSvgIcon-colorError');
  });
});

