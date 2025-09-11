import axios from 'axios';
import { crearRegistro } from './alimentacionService';
import { API_ALIMENTACION_URL } from '../config';

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    post: jest.fn(),
  },
}));

describe('alimentacionService', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('conserva cantidadAlimentoSolido como número en el payload', () => {
    axios.post.mockResolvedValue({});
    const usuarioId = 1;
    const bebeId = 2;
    const data = { cantidadAlimentoSolido: '5', tipoAlimentacionId: 3 };
    const API_ALIMENTACION_ENDPOINT = `${API_ALIMENTACION_URL}/api/v1/alimentacion`;

    crearRegistro(usuarioId, bebeId, data);

    expect(axios.post).toHaveBeenCalledWith(
      `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
      expect.objectContaining({
        cantidadAlimentoSolido: 5,
        tipoAlimentacion: { id: 3 },
      })
    );
  });
});

