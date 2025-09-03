import axios from 'axios';
import { obtenerStatsRapidas } from './cuidadosService';
import { API_CUIDADOS_URL } from '../config';

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
  },
}));

describe('cuidadosService', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('obtenerStatsRapidas realiza la llamada HTTP correcta', () => {
    axios.get.mockResolvedValue({});
    const usuarioId = 1;
    const bebeId = 2;
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    obtenerStatsRapidas(usuarioId, bebeId);

    expect(axios.get).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`
    );
  });
});
