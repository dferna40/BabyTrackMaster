import axios from 'axios';
import { API_BEBE_URL } from '../config';

const API_BEBES_ENDPOINT = `${API_BEBE_URL}/api/v1/bebes`;

export const crearBebe = (payload, headers = {}) => {
  return axios.post(API_BEBES_ENDPOINT, payload, {
    headers: { 'Content-Type': 'application/json', ...headers },
  });
};
