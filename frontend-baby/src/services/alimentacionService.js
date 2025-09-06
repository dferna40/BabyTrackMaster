import axios from 'axios';
import { API_ALIMENTACION_URL } from '../config';

const API_ALIMENTACION_ENDPOINT = `${API_ALIMENTACION_URL}/api/v1/alimentaciones`;

export const listarPorBebe = (usuarioId, bebeId) => {
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`
  );
};

export const listarRecientes = (usuarioId, bebeId, limit) => {
  const params = {};
  if (limit !== undefined) params.limit = limit;
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/recientes`,
    { params }
  );
};

export const obtenerEstadisticas = (usuarioId, bebeId) => {
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/estadisticas`
  );
};

export const crearRegistro = (usuarioId, data) => {
  return axios.post(`${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarRegistro = (usuarioId, id, data) => {
  return axios.put(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/${id}`,
    data
  );
};

export const eliminarRegistro = (usuarioId, id) => {
  return axios.delete(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/${id}`
  );
};
