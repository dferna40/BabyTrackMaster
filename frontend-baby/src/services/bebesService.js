import axios from 'axios';
import { API_USUARIOS_URL } from '../config';

const API_BEBES_ENDPOINT = `${API_USUARIOS_URL}/api/v1/bebes`;

export const crearBebe = (payload) => {
  return axios.post(API_BEBES_ENDPOINT, payload);
};
