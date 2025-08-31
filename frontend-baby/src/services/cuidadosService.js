import axios from 'axios';
import { API_CUIDADOS_URL } from '../config';

const API_CUIDADOS_ENDPOINT = `${API_CUIDADOS_URL}/api/v1/cuidados`;

export const listarPorBebe = (bebeId, page, size) => {
  const params = {};
  if (page !== undefined) params.page = page;
  if (size !== undefined) params.size = size;
  return axios.get(`${API_CUIDADOS_ENDPOINT}/bebe/${bebeId}`, { params });
};

export const listarRecientes = (bebeId, limit = 5) => {
  return axios.get(`${API_CUIDADOS_ENDPOINT}/bebe/${bebeId}`, {
    params: { limit },
  });
};

export const crearCuidado = (data) => {
  return axios.post(`${API_CUIDADOS_ENDPOINT}`, data);
};

export const actualizarCuidado = (id, data) => {
  return axios.put(`${API_CUIDADOS_ENDPOINT}/${id}`, data);
};

export const eliminarCuidado = (id) => {
  return axios.delete(`${API_CUIDADOS_ENDPOINT}/${id}`);
};

