import apiClient from '../../apiConfig';

const ENDPOINT = '/api/diem/diem-chi-tiet';

export const createDiemChiTiet = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDiemChiTiet = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDiemChiTiet = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDiemChiTiet,
  updateDiemChiTiet,
  deleteDiemChiTiet,
};
