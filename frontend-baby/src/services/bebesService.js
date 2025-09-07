import axios from 'axios';
import { API_BEBE_URL } from '../config';

const API_BEBES_ENDPOINT = `${API_BEBE_URL}/api/v1/bebes`;
const API_TIPO_ALERGIAS_ENDPOINT = `${API_BEBE_URL}/api/v1/tipo-alergias`;
const API_TIPO_GRUPO_SANGUINEO_ENDPOINT = `${API_BEBE_URL}/api/v1/tipo-gruposanguineo`;

export const crearBebe = (payload, headers = {}) => {
  return axios.post(API_BEBES_ENDPOINT, payload, {
    headers: { 'Content-Type': 'application/json', ...headers },
  });
};

export const getBebes = () => {
  return axios.get(API_BEBES_ENDPOINT);
};

export const getBebesByUsuario = (usuarioId) => {
  return axios.get(`${API_BEBES_ENDPOINT}?usuarioId=${usuarioId}`);
};

export const getBebeById = (id) => {
  return axios.get(`${API_BEBES_ENDPOINT}/${id}`);
};

export const actualizarBebe = (id, payload, headers = {}) => {
  return axios.put(`${API_BEBES_ENDPOINT}/${id}`, payload, {
    headers: { 'Content-Type': 'application/json', ...headers },
  });
};

export const eliminarBebe = (id) => {
  return axios.delete(`${API_BEBES_ENDPOINT}/${id}`);
};

export const fetchTipoAlergias = () => {
  return axios.get(API_TIPO_ALERGIAS_ENDPOINT);
};

export const fetchTipoGrupoSanguineo = () => {
  return axios.get(API_TIPO_GRUPO_SANGUINEO_ENDPOINT);
};
