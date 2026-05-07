import apiClient from '../../apiConfig';

const ENDPOINT = '/api/lms/ket-qua-bai-tap-lop';

export const createKetQuaBaiTapLop = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateKetQuaBaiTapLop = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteKetQuaBaiTapLop = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createKetQuaBaiTapLop,
  updateKetQuaBaiTapLop,
  deleteKetQuaBaiTapLop,
};
