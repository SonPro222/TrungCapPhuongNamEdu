import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/diem-danh';

export const createDiemDanh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDiemDanh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDiemDanh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDiemDanh,
  updateDiemDanh,
  deleteDiemDanh,
};
