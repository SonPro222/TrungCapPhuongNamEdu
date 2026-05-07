import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/dieu-kien-tot-nghiep';

export const createDieuKienTotNghiep = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateDieuKienTotNghiep = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteDieuKienTotNghiep = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createDieuKienTotNghiep,
  updateDieuKienTotNghiep,
  deleteDieuKienTotNghiep,
};
