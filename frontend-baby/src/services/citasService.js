import axios from 'axios';
import { API_CITAS_URL } from '../config';

const API_CITAS_ENDPOINT = `${API_CITAS_URL}/api/v1/citas`;

export const listarPorBebe = (usuarioId, bebeId) => {
  return axios.get(`${API_CITAS_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`);
};

export const crearCita = (usuarioId, data) => {
  return axios.post(`${API_CITAS_ENDPOINT}/usuario/${usuarioId}`, data);
};

export const actualizarCita = (usuarioId, id, data) => {
  return axios.put(`${API_CITAS_ENDPOINT}/usuario/${usuarioId}/${id}`, data);
};

export const eliminarCita = (usuarioId, id) => {
  return axios.delete(`${API_CITAS_ENDPOINT}/usuario/${usuarioId}/${id}`);
};

export const listarTipos = () => {
  return axios.get(`${API_CITAS_ENDPOINT}/tipos`);
};

export const enviarRecordatorio = (usuarioId, id) => {
  return axios.post(`${API_CITAS_ENDPOINT}/usuario/${usuarioId}/${id}/recordatorio`);
};

