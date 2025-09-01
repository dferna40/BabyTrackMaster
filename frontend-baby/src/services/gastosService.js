import axios from 'axios';
import { API_GASTOS_URL } from '../config';

const API_GASTOS_ENDPOINT = `${API_GASTOS_URL}/api/v1/gastos`;
const API_CATEGORIAS_ENDPOINT = `${API_GASTOS_URL}/api/v1/categorias`;

export const listarPorBebe = (usuarioId, bebeId, page = 0, size = 10) => {
  return axios.get(
    `${API_GASTOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    {
      params: { page, size },
    }
  );
};

export const listarRecientes = (usuarioId, bebeId, limit = 5) => {
  return axios.get(
    `${API_GASTOS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    {
      params: { limit },
    }
  );
};

export const crearGasto = (usuarioId, data) => {
  return axios.post(`${API_GASTOS_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarGasto = (usuarioId, id, data) => {
  return axios.put(
    `${API_GASTOS_ENDPOINT}/usuario/${usuarioId}/${id}`,
    data
  );
};

export const eliminarGasto = (usuarioId, id) => {
  return axios.delete(`${API_GASTOS_ENDPOINT}/usuario/${usuarioId}/${id}`);
};

export const listarCategorias = () => {
  return axios.get(API_CATEGORIAS_ENDPOINT);
};

