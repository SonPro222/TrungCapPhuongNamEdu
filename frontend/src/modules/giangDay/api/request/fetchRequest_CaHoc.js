import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/giang-day/ca-hoc';

export const createCaHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateCaHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteCaHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createCaHoc,
  updateCaHoc,
  deleteCaHoc,
};
