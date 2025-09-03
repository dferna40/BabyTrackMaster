import axios from 'axios';
import { API_RUTINAS_URL } from '../config';

const API_RUTINAS_ENDPOINT = `${API_RUTINAS_URL}/api/v1/rutinas`;

export const listarPorBebe = (usuarioId, bebeId, page = 0, size = 10) => {
  return axios.get(
    `${API_RUTINAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    {
      params: { page, size },
    }
  );
};

export const crearRutina = (usuarioId, bebeId, data) => {
  return axios.post(
    `${API_RUTINAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    data
  );
};

export const actualizarRutina = (usuarioId, bebeId, id, data) => {
  return axios.put(
    `${API_RUTINAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`,
    data
  );
};

export const eliminarRutina = (usuarioId, bebeId, id) => {
  return axios.delete(
    `${API_RUTINAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`
  );
};

export const duplicarRutina = (usuarioId, bebeId, id) => {
  return axios.post(
    `${API_RUTINAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}/duplicar`
  );
};

export default {
  listarPorBebe,
  crearRutina,
  actualizarRutina,
  eliminarRutina,
  duplicarRutina,
};
