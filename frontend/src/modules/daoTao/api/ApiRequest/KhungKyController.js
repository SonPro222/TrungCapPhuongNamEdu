import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/dao-tao/khung-ky';

export const createKhungKy = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateKhungKy = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteKhungKy = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createKhungKy,
  updateKhungKy,
  deleteKhungKy,
};
