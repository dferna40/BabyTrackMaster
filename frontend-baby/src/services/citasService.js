import axios from 'axios';
import dayjs from 'dayjs';
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

export const listarProximas = (bebeId, limite = 10) => {
  return listar(bebeId, 0, 100).then((response) => {
    const items = Array.isArray(response.data)
      ? response.data
      : response.data?.content;
    if (!Array.isArray(items)) return [];
    const now = dayjs();
    return items
      .map((c) => ({
        ...c,
        tipoNombre: c.tipo?.nombre ?? c.tipoNombre,
        estadoNombre: c.estado?.nombre ?? c.estadoNombre,
      }))
      .filter((c) => dayjs(`${c.fecha}T${c.hora}`) >= now)
      .sort((a, b) =>
        dayjs(`${a.fecha}T${a.hora}`).diff(dayjs(`${b.fecha}T${b.hora}`))
      )
      .slice(0, limite);
  });
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

export const enviarRecordatorio = (id, minutosAntelacion) => {
  return axios.post(`${API_CITAS_ENDPOINT}/${id}/recordatorio`, {
    minutosAntelacion,
  });
};

