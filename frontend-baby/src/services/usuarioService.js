import axios from 'axios';
import { API_USUARIOS_URL } from '../config';

const API_USUARIOS_ENDPOINT = `${API_USUARIOS_URL}/api/v1/usuarios`;

export const getCurrentUser = () => {
  return axios.get(`${API_USUARIOS_ENDPOINT}/me`);
};

