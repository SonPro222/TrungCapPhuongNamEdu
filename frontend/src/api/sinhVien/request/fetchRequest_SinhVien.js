import apiClient from '../../apiConfig';

const ENDPOINT = '/api/sinh-vien/sinh-vien';

export const createSinhVien = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSinhVien = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSinhVien = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSinhVien,
  updateSinhVien,
  deleteSinhVien,
};
