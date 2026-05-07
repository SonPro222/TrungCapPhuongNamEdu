import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/he-thong/vai-tro';

export const createVaiTro = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateVaiTro = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteVaiTro = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createVaiTro,
  updateVaiTro,
  deleteVaiTro,
};
