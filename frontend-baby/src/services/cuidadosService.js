import axios from 'axios';
import { API_CUIDADOS_URL } from '../config';

const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

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
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    {
      params: { limit },
    }
  );
};

export const crearCuidado = (usuarioId, data) => {
  return axios.post(`${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarCuidado = (usuarioId, id, data) => {
  return axios.put(
    `${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
    data
  );
};

export const eliminarCuidado = (usuarioId, id) => {
  return axios.delete(`${API_CUIDADOS_ENDPOINT}/usuario/${usuarioId}/${id}`);
};

