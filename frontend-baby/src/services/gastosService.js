import axios from 'axios';
import API_BASE_URL from '../config';

const API_GASTOS_URL = `${API_BASE_URL}/api/v1/gastos`;
const API_CATEGORIAS_URL = `${API_BASE_URL}/api/v1/categorias`;

export const listarPorBebe = (bebeId, page, size) => {
  const params = {};
  if (page !== undefined) params.page = page;
  if (size !== undefined) params.size = size;
  return axios.get(`${API_GASTOS_URL}/bebe/${bebeId}`, { params });
};

export const listarRecientes = (bebeId, limit = 5) => {
  return axios.get(`${API_GASTOS_URL}/bebe/${bebeId}`, {
    params: { limit },
  });
};

export const crearGasto = (data) => {
  return axios.post(`${API_GASTOS_URL}`, data);
};

export const actualizarGasto = (id, data) => {
  return axios.put(`${API_GASTOS_URL}/${id}`, data);
};

export const eliminarGasto = (id) => {
  return axios.delete(`${API_GASTOS_URL}/${id}`);
};

export const listarCategorias = () => {
  return axios.get(API_CATEGORIAS_URL);
};

