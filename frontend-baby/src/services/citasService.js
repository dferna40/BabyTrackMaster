import axios from 'axios';
import { API_CITAS_URL } from '../config';

const API_CITAS_ENDPOINT = `${API_CITAS_URL}/api/v1/citas`;
const API_TIPOS_CITA_ENDPOINT = `${API_CITAS_URL}/api/v1/tipos-cita`;
const API_ESTADOS_CITA_ENDPOINT = `${API_CITAS_URL}/api/v1/estados-cita`;

export const listar = (bebeId, page, size) => {
  const params = {};
  if (page !== undefined) params.page = page;
  if (size !== undefined) params.size = size;
  return axios.get(`${API_CITAS_ENDPOINT}/bebe/${bebeId}`, { params });
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

export const confirmarCita = (id) => {
  return axios.put(`${API_CITAS_ENDPOINT}/${id}/confirmar`);
};

export const cancelarCita = (id) => {
  return axios.put(`${API_CITAS_ENDPOINT}/${id}/cancelar`);
};

export const completarCita = (id) => {
  return axios.put(`${API_CITAS_ENDPOINT}/${id}/completar`);
};

export const marcarNoAsistida = (id) => {
  return axios.put(`${API_CITAS_ENDPOINT}/${id}/no-asistida`);
};

export const listarTipos = () => {
  return axios.get(`${API_TIPOS_CITA_ENDPOINT}`);
};

export const listarEstados = () => {
  return axios.get(`${API_ESTADOS_CITA_ENDPOINT}`);
};

export const enviarRecordatorio = (id) => {
  return axios.post(`${API_CITAS_ENDPOINT}/${id}/recordatorio`);
};

