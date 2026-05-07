import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/lms/dot-giao-bai-online';

export const createDotGiaoBaiOnline = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDotGiaoBaiOnline = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDotGiaoBaiOnline = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDotGiaoBaiOnline,
  updateDotGiaoBaiOnline,
  deleteDotGiaoBaiOnline,
};
