import { render, screen, waitFor } from '@testing-library/react';
import Crecimiento from './Crecimiento';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';

jest.mock('../../services/crecimientoService', () => ({
  listarPorBebe: jest.fn(),
  crearRegistro: jest.fn(),
  actualizarRegistro: jest.fn(),
  eliminarRegistro: jest.fn(),
  listarTipos: jest.fn(),
}));

jest.mock('../components/CrecimientoForm', () => () => null);

jest.mock('@mui/x-charts/LineChart', () => ({
  LineChart: () => null,
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

const { listarPorBebe, listarTipos } = require('../../services/crecimientoService');

describe('Crecimiento', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra mensaje y oculta tabla y gráfica cuando no hay registros', async () => {
    listarPorBebe.mockResolvedValue({ data: [] });
    listarTipos.mockResolvedValue({ data: [{ id: 1, nombre: 'Peso' }] });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 1 } }}>
          <Crecimiento />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarPorBebe).toHaveBeenCalled());

    expect(
      await screen.findByText('Aún no se han insertado datos de peso')
    ).toBeInTheDocument();
    expect(screen.queryByRole('table')).not.toBeInTheDocument();
    expect(screen.queryByText('Evolución')).not.toBeInTheDocument();
  });
});
