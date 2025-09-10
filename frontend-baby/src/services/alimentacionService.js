import axios from 'axios';
import { API_ALIMENTACION_URL } from '../config';

const API_ALIMENTACION_ENDPOINT = `${API_ALIMENTACION_URL}/api/v1/alimentacion`;

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
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`
  );
};

export const crearRegistro = (usuarioId, bebeId, data) =>
  axios.post(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    data
  );

export const actualizarRegistro = (usuarioId, bebeId, id, data) =>
  axios.put(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`,
    data
  );

export const eliminarRegistro = (usuarioId, bebeId, id) =>
  axios.delete(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`
  );

export const listarTiposLactancia = () =>
  axios.get(`${API_ALIMENTACION_ENDPOINT}/tipos-lactancia`);
