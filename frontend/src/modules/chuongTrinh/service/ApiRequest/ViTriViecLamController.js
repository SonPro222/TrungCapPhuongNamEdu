import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/vi-tri-viec-lam';

export const createViTriViecLam = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateViTriViecLam = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteViTriViecLam = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createViTriViecLam,
  updateViTriViecLam,
  deleteViTriViecLam,
};
