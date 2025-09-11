import axios from 'axios';
import { API_ALIMENTACION_URL } from '../config';

const API_ALIMENTACION_ENDPOINT = `${API_ALIMENTACION_URL}/api/v1/alimentacion`;

export const listarPorBebe = (usuarioId, bebeId) => {
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`
  );
};

export const listarRecientes = (usuarioId, bebeId, limit) => {
  const params = {};
  if (limit !== undefined) params.limit = limit;
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/recientes`,
    { params }
  );
};

export const obtenerEstadisticas = (usuarioId, bebeId, tipoAlimentacionId) => {
  const params = {};
  if (tipoAlimentacionId !== undefined) params.tipoAlimentacionId = tipoAlimentacionId;
  return axios.get(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/stats`,
    { params }
  );
};

const buildPayload = (data) => {
  const payload = {
    ...data,
    tipoAlimentacion: data.tipoAlimentacionId
      ? { id: data.tipoAlimentacionId }
      : undefined,
    tipoLactancia: data.tipoLactanciaId
      ? { id: data.tipoLactanciaId }
      : undefined,
    tipoBiberon: data.tipoBiberonId ? { id: data.tipoBiberonId } : undefined,
    tipoAlimentacionSolido: data.tipoAlimentacionSolidoId
      ? { id: data.tipoAlimentacionSolidoId }
      : undefined,
    alimentacionOtros: data.alimentacionOtros,
  };
  delete payload.tipoAlimentacionId;
  delete payload.tipoLactanciaId;
  delete payload.tipoBiberonId;
  delete payload.tipoAlimentacionSolidoId;
  return payload;
};

export const crearRegistro = (usuarioId, bebeId, data) =>
  axios.post(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`,
    buildPayload(data)
  );

export const actualizarRegistro = (usuarioId, bebeId, id, data) =>
  axios.put(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`,
    buildPayload(data)
  );

export const eliminarRegistro = (usuarioId, bebeId, id) =>
  axios.delete(
    `${API_ALIMENTACION_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}/${id}`
  );

export const listarTiposLactancia = () =>
  axios.get(`${API_ALIMENTACION_ENDPOINT}/tipos-lactancia`);

export const listarTiposAlimentacion = () =>
  axios.get(`${API_ALIMENTACION_ENDPOINT}/tipos-alimentacion`);

export const listarTiposBiberon = () =>
  axios.get(`${API_ALIMENTACION_ENDPOINT}/tipos-biberon`);

export const listarTiposAlimentacionSolidos = () =>
  axios.get(`${API_ALIMENTACION_ENDPOINT}/tipos-alimentacion-solido`);
