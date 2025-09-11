import axios from 'axios';
import { API_CUIDADOS_URL } from '../config';

const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;
const API_TIPOS_CUIDADO_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/tipos-cuidado`;
const API_TIPOS_PANAL_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/tipos-panal`;

export const listarPorBebe = (usuarioId, bebeId, page, size) => {
  const params = {};
  if (page !== undefined) params.page = page;
  if (size !== undefined) params.size = size;
  return axios.get(
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    { params }
  );
};

export const listarRecientes = (usuarioId, bebeId, limit = 5) => {
  return axios.get(
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/recientes`,
    {
      params: { limit },
    }
  );
};

export const obtenerStatsRapidas = (usuarioId, bebeId, fecha) => {
  return axios.get(
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`,
    {
      params: { fecha },
    }
  );
};

export const crearCuidado = (usuarioId, data) => {
  const payload = { ...data, tipoPanalId: data.tipoPanalId };
  return axios.post(`${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}`, payload);
};

export const actualizarCuidado = (usuarioId, id, data) => {
  const payload = { ...data, tipoPanalId: data.tipoPanalId };
  return axios.put(
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
    payload
  );
};

export const eliminarCuidado = (usuarioId, id) => {
  return axios.delete(`${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`);
};

export const listarTipos = () => {
  return axios.get(`${API_TIPOS_CUIDADO_ENDPOINT}`);
};

export const listarTiposPanal = () => {
  return axios.get(`${API_TIPOS_PANAL_ENDPOINT}`);
};

