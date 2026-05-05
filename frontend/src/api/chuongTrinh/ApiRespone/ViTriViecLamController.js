import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/vi-tri-viec-lam';

export const getAllViTriViecLam = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getViTriViecLamById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllViTriViecLam,
  getViTriViecLamById,
};
