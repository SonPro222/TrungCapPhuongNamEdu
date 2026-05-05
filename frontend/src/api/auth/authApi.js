// src/api/auth/authApi.js
import apiClient, { BASE_URL } from '@/api/apiConfig';

export const loginThuong = (data) => {
    return apiClient.post('/api/auth/login', data);
};

export const loginGoogle = () => {
    window.location.href = `${BASE_URL}/oauth2/authorization/google`;
};

export default {
    loginThuong,
    loginGoogle,
};