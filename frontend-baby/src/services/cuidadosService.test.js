import axios from 'axios';
import {
  obtenerStatsRapidas,
  crearCuidado,
  actualizarCuidado,
  listarTiposPanal,
} from './cuidadosService';
import { API_CUIDADOS_URL } from '../config';

jest.mock('axios', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
  },
}));

describe('cuidadosService', () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it('obtenerStatsRapidas realiza la llamada HTTP correcta sin fecha', () => {
    axios.get.mockResolvedValue({});
    const usuarioId = 1;
    const bebeId = 2;
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    obtenerStatsRapidas(usuarioId, bebeId);

    expect(axios.get).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`,
      { params: { fecha: undefined } }
    );
  });

  it('obtenerStatsRapidas incluye fecha cuando se proporciona', () => {
    axios.get.mockResolvedValue({});
    const usuarioId = 1;
    const bebeId = 2;
    const fecha = 123;
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    obtenerStatsRapidas(usuarioId, bebeId, fecha);

    expect(axios.get).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`,
      { params: { fecha } }
    );
  });

  it('listarTiposPanal realiza la llamada HTTP correcta', () => {
    axios.get.mockResolvedValue({});
    const API_TIPOS_PANAL_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/tipos-panal`;

    listarTiposPanal();

    expect(axios.get).toHaveBeenCalledWith(API_TIPOS_PANAL_ENDPOINT);
  });

  it('crearCuidado envía tipoPanalId cuando está presente', () => {
    axios.post.mockResolvedValue({});
    const usuarioId = 1;
    const data = { bebeId: 2, tipoPanalId: 3 };
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    crearCuidado(usuarioId, data);

    expect(axios.post).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}`,
      expect.objectContaining({ tipoPanalId: 3 })
    );
  });

  it('actualizarCuidado envía tipoPanalId cuando está presente', () => {
    axios.put.mockResolvedValue({});
    const usuarioId = 1;
    const id = 5;
    const data = { bebeId: 2, tipoPanalId: 4 };
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    actualizarCuidado(usuarioId, id, data);

    expect(axios.put).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
      expect.objectContaining({ tipoPanalId: 4 })
    );
  });

  it('crearCuidado transmite cantidadPanal sin alterarlo', () => {
    axios.post.mockResolvedValue({});
    const usuarioId = 1;
    const data = { bebeId: 2, cantidadPanal: 5 };
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    crearCuidado(usuarioId, data);

    expect(axios.post).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}`,
      expect.objectContaining({ cantidadPanal: 5 })
    );
  });

  it('actualizarCuidado transmite cantidadPanal sin alterarlo', () => {
    axios.put.mockResolvedValue({});
    const usuarioId = 1;
    const id = 5;
    const data = { bebeId: 2, cantidadPanal: 7 };
    const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

    actualizarCuidado(usuarioId, id, data);

    expect(axios.put).toHaveBeenCalledWith(
      `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
      expect.objectContaining({ cantidadPanal: 7 })
    );
  });
});
