import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import Citas from './Citas';
import { AuthContext } from '../../context/AuthContext';
import { BabyContext } from '../../context/BabyContext';
jest.mock('../../services/citasService', () => ({
  listar: jest.fn(),
  crearCita: jest.fn(),
  actualizarCita: jest.fn(),
  confirmarCita: jest.fn(),
  cancelarCita: jest.fn(),
  completarCita: jest.fn(),
  marcarNoAsistida: jest.fn(),
  listarTipos: jest.fn(),
  listarEstados: jest.fn(),
  enviarRecordatorio: jest.fn(),
}));

const {
  listar,
  listarTipos,
  listarEstados,
} = require('../../services/citasService');

jest.mock('../components/CitaForm', () => () => null);

jest.mock('@mui/x-date-pickers', () => ({
  DateCalendar: () => null,
  PickersDay: () => null,
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

describe('Citas', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('muestra mensaje cuando no se han registrado citas', async () => {
    listar.mockResolvedValue({ data: [] });
    listarTipos.mockResolvedValue({ data: [] });
    listarEstados.mockResolvedValue({ data: [] });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 1 } }}>
          <Citas />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listar).toHaveBeenCalled());

    expect(
      await screen.findByText('No se han registrado citas')
    ).toBeInTheDocument();
  });

  it('muestra mensaje cuando no hay citas en la semana', async () => {
    listar.mockResolvedValue({ data: [] });
    listarTipos.mockResolvedValue({ data: [] });
    listarEstados.mockResolvedValue({ data: [] });

    render(
      <AuthContext.Provider value={{ user: { id: 1 } }}>
        <BabyContext.Provider value={{ activeBaby: { id: 1 } }}>
          <Citas />
        </BabyContext.Provider>
      </AuthContext.Provider>
    );

    await waitFor(() => expect(listar).toHaveBeenCalled());

    fireEvent.click(screen.getByRole('button', { name: /semana/i }));

    expect(
      await screen.findByText('No hay citas para esta semana')
    ).toBeInTheDocument();
  });
});
