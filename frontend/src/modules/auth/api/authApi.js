import axiosClient, { unwrapResponse } from '@/core/api/axiosClient.js';

const AUTH_BASE = '/api/auth';
const AUTH_V1_BASE = '/api/v1/auth';

const postWithFallback = async (primaryUrl, fallbackUrl, payload) => {
  try {
    const response = await axiosClient.post(primaryUrl, payload);
    return unwrapResponse(response);
  } catch (error) {
    if (!fallbackUrl || ![404, 405].includes(error?.response?.status)) throw error;
    const response = await axiosClient.post(fallbackUrl, payload);
    return unwrapResponse(response);
  }
};

export const authApi = {
  login(payload) {
    return postWithFallback(`${AUTH_BASE}/login`, `${AUTH_V1_BASE}/login`, payload);
  },
  register(payload) {
    return postWithFallback(`${AUTH_BASE}/register`, `${AUTH_V1_BASE}/register`, payload);
  },
  logout() {
    return axiosClient.post(`${AUTH_BASE}/logout`).catch(() => null);
  },
  profile() {
    return axiosClient.get('/profile').then(unwrapResponse);
  },
};

export default authApi;
