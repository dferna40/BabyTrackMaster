import axios from 'axios';
import { API_CITAS_URL } from '../config';

const API_CITAS_ENDPOINT = `${API_CITAS_URL}/api/v1/citas`;
const API_TIPOS_CITA_ENDPOINT = `${API_CITAS_URL}/api/v1/tipos-cita`;

export const listar = () => {
  return axios.get(`${API_CITAS_ENDPOINT}`);
};

export const crearCita = (data) => {
  return axios.post(`${API_CITAS_ENDPOINT}`, data);
};

export const actualizarCita = (id, data) => {
  return axios.put(`${API_CITAS_ENDPOINT}/${id}`, data);
};

export const eliminarCita = (id) => {
  return axios.delete(`${API_CITAS_ENDPOINT}/${id}`);
};

export const listarTipos = () => {
  return axios.get(`${API_TIPOS_CITA_ENDPOINT}`);
};

export const enviarRecordatorio = (id) => {
  return axios.post(`${API_CITAS_ENDPOINT}/${id}/recordatorio`);
};

