import axios from 'axios';
import { API_ALIMENTACION_URL } from '../config';

const API_ALIMENTACION_ENDPOINT = `${API_ALIMENTACION_URL}/api/v1/alimentaciones`;

export const listarPorBebe = (usuarioId, bebeId, page, size) => {
  const params = {};
  if (page !== undefined) params.page = page;
  if (size !== undefined) params.size = size;
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    { params }
  );
};

export const crearAlimentacion = (usuarioId, data) => {
  return axios.post(`${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarAlimentacion = (usuarioId, id, data) => {
  return axios.put(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/${id}`,
    data
  );
};

export const eliminarAlimentacion = (usuarioId, id) => {
  return axios.delete(`${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/${id}`);
};

