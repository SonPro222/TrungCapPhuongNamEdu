import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/nhom-tu-chon';

export const createNhomTuChon = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNhomTuChon = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNhomTuChon = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNhomTuChon,
  updateNhomTuChon,
  deleteNhomTuChon,
};
