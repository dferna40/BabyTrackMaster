import { render, screen, waitFor } from '@testing-library/react';
import Cuidados from './Cuidados';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
import { listarPorBebe, listarTipos } from '../../services/cuidadosService';

jest.mock('../../services/cuidadosService', () => ({
  listarPorBebe: jest.fn(),
  listarTipos: jest.fn(),
  crearCuidado: jest.fn(),
  actualizarCuidado: jest.fn(),
  eliminarCuidado: jest.fn(),
  listarTiposPanal: jest.fn(),
}));

jest.mock('jspdf', () => ({
  __esModule: true,
  default: jest.fn().mockImplementation(() => ({ save: jest.fn() })),
}));

jest.mock('jspdf-autotable', () => jest.fn());

jest.mock('@mui/x-charts/BarChart', () => ({
  BarChart: () => null,
}));

jest.mock(
  'react-router-dom',
  () => ({
    useLocation: () => ({ state: {} }),
  }),
  { virtual: true }
);

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  },
}));

jest.mock('../components/CuidadoForm', () => () => null);

describe('Cuidados', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra columnas para cuidados de pañal', async () => {
    listarTipos.mockResolvedValue({ data: [{ id: 1, nombre: 'Pañal' }] });
    listarPorBebe.mockResolvedValue({
      data: [
        {
          id: 1,
          tipoId: 1,
          tipoNombre: 'Pañal',
          inicio: new Date().toISOString(),
          tipoPanalNombre: 'Pipi',
          cantidadPanal: '2',
        },
      ],
    });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 1 } }}>
          <Cuidados />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listarTipos).toHaveBeenCalled());
    await waitFor(() => expect(listarPorBebe).toHaveBeenCalled());

    expect(await screen.findByText('Tipo pañal')).toBeInTheDocument();
    expect(screen.getByText('Cantidad')).toBeInTheDocument();
    expect(screen.getByText('Pipi')).toBeInTheDocument();
    expect(screen.getByText('2')).toBeInTheDocument();
  });
});
