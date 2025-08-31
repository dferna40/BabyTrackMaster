import axios from 'axios';
import { API_GASTOS_URL } from '../config';

const API_GASTOS_ENDPOINT = `${API_GASTOS_URL}/api/v1/gastos`;
const API_CATEGORIAS_ENDPOINT = `${API_GASTOS_URL}/api/v1/categorias`;

export const listarPorBebe = (bebeId, page = 0, size = 10) => {
  return axios.get(`${API_GASTOS_ENDPOINT}/bebe/${bebeId}`, {
    params: { page, size },
  });
};

export const listarRecientes = (bebeId, limit = 5) => {
  return axios.get(`${API_GASTOS_ENDPOINT}/bebe/${bebeId}`, {
    params: { limit },
  });
};

export const crearGasto = (data) => {
  return axios.post(`${API_GASTOS_ENDPOINT}`, data);
};

export const actualizarGasto = (id, data) => {
  return axios.put(`${API_GASTOS_ENDPOINT}/${id}`, data);
};

export const eliminarGasto = (id) => {
  return axios.delete(`${API_GASTOS_ENDPOINT}/${id}`);
};

export const listarCategorias = () => {
  return axios.get(API_CATEGORIAS_ENDPOINT);
};

