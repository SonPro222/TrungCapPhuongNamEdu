import apiClient from '../../apiConfig';

const ENDPOINT = '/api/he-thong/vai-tro-quyen';

export const createVaiTroQuyen = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateVaiTroQuyen = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteVaiTroQuyen = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createVaiTroQuyen,
  updateVaiTroQuyen,
  deleteVaiTroQuyen,
};
