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
  });

  it('muestra tipoPanalNombre para cuidados de pañal', async () => {
    listarRecientes.mockResolvedValue({
      data: [
        {
          id: 1,
          tipoNombre: 'Pañal',
          inicio: '2024-01-01T10:00',
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
});
