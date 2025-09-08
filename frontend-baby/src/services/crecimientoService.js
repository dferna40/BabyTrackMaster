import axios from 'axios';
import { API_CRECIMIENTO_URL } from '../config';

const API_CRECIMIENTOS_ENDPOINT = `${API_CRECIMIENTO_URL}/api/v1/crecimientos`;
const API_TIPOS_CRECIMIENTO_ENDPOINT = `${API_CRECIMIENTO_URL}/api/v1/tipos-crecimiento`;

export const listarPorBebe = (usuarioId, bebeId, limit) => {
  const params = {};
  if (limit !== undefined) params.limit = limit;
  return axios.get(
    `${API_CRECIMIENTOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    { params }
  );
};

export const crearRegistro = (usuarioId, data) => {
  return axios.post(`${API_CRECIMIENTOS_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarRegistro = (usuarioId, id, data) => {
  return axios.put(
    `${API_CRECIMIENTOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
    data
  );
};

export const eliminarRegistro = (usuarioId, id) => {
  return axios.delete(
    `${API_CRECIMIENTOS_ENDPOINT}/usuario/${usuarioId}/${id}`
  );
};

export const listarTipos = () => {
  return axios.get(`${API_TIPOS_CRECIMIENTO_ENDPOINT}`);
};

