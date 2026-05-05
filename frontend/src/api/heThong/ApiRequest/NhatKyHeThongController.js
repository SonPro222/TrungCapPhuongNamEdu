import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/nhat-ky-he-thong';

export const createNhatKyHeThong = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateNhatKyHeThong = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteNhatKyHeThong = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createNhatKyHeThong,
  updateNhatKyHeThong,
  deleteNhatKyHeThong,
};
