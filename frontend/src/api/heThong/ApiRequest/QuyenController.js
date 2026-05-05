import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/quyen';

export const createQuyen = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateQuyen = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteQuyen = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createQuyen,
  updateQuyen,
  deleteQuyen,
};
