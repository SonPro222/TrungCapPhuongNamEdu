import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/giao-vien';

export const createGiaoVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateGiaoVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteGiaoVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createGiaoVien,
  updateGiaoVien,
  deleteGiaoVien,
};
