import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/dieu-kien-mon-hoc';

export const createDieuKienMonHoc = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDieuKienMonHoc = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDieuKienMonHoc = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDieuKienMonHoc,
  updateDieuKienMonHoc,
  deleteDieuKienMonHoc,
};
