import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/chuongTrinh/chuong-trinh-mon';

export const createChuongTrinhMon = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateChuongTrinhMon = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteChuongTrinhMon = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createChuongTrinhMon,
  updateChuongTrinhMon,
  deleteChuongTrinhMon,
};
