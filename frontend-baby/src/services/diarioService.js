import axios from 'axios';
import { API_DIARIO_URL } from '../config';

const API_DIARIO_ENDPOINT = `${API_DIARIO_URL}/api/v1/diario`;

export const listarEntradas = (usuarioId, bebeId) => {
  return axios
    .get(`${API_DIARIO_ENDPOINT}/usuario/${usuarioId}/bebe/${bebeId}`)
    .then((res) =>
      res.data.map((e) => ({
        id: e.id,
        texto: e.contenido,
        emocion: e.estadoAnimo,
        fecha: e.fecha,
        etiquetas: e.etiquetas ? e.etiquetas.split(',') : [],
      }))
    );
};

export const crearEntrada = (usuarioId, data) => {
  return axios.post(`${API_DIARIO_ENDPOINT}/usuario/${usuarioId}`, data);
};

