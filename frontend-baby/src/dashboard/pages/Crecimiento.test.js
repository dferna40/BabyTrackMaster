import { render, screen, fireEvent, waitFor, within } from '@testing-library/react';
import Crecimiento from './Crecimiento';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { listarPorBebe, listarTipos } from '../../services/crecimientoService';

jest.mock('../../services/crecimientoService', () => ({
  listarPorBebe: jest.fn(),
  listarTipos: jest.fn(),
  crearRegistro: jest.fn(),
  actualizarRegistro: jest.fn(),
  eliminarRegistro: jest.fn(),
}));

jest.mock('../components/CrecimientoForm', () => () => null);

jest.mock('@mui/x-charts/LineChart', () => ({
  LineChart: ({ series }) => (
    <div data-testid="chart">
      {series.map((s, i) => (
        <div key={i}>{s.label}</div>
      ))}
    </div>
  ),
}));

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  },
}));

describe('Crecimiento', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra etiquetas correctas por pestaña', async () => {
    listarTipos.mockResolvedValue({
      data: [
        { id: 1, nombre: 'Peso' },
        { id: 2, nombre: 'Talla' },
        { id: 3, nombre: 'Perímetro cefálico' },
      ],
    });
    listarPorBebe.mockResolvedValue({ data: [] });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 1 } }}>
          <Crecimiento />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarTipos).toHaveBeenCalled());

    expect(await screen.findByText('Peso (kg)')).toBeInTheDocument();
    expect(await screen.findByText('Evolución del peso')).toBeInTheDocument();
    expect(
      within(await screen.findByTestId('chart')).getByText('Peso')
    ).toBeInTheDocument();

    fireEvent.click(screen.getByRole('tab', { name: 'Talla' }));

    expect(await screen.findByText('Medida (cm)')).toBeInTheDocument();
    expect(await screen.findByText('Evolución de la talla')).toBeInTheDocument();
    expect(
      within(await screen.findByTestId('chart')).getByText('Talla')
    ).toBeInTheDocument();

    fireEvent.click(screen.getByRole('tab', { name: 'Perímetro cefálico' }));

    expect(
      await screen.findByText('Evolución del perímetro cefálico')
    ).toBeInTheDocument();
    expect(
      within(await screen.findByTestId('chart')).getByText('Perímetro cefálico')
    ).toBeInTheDocument();
  });
});
