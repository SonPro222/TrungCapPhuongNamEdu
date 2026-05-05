import apiClient from '../../apiConfig';

const ENDPOINT = '/api/sinh-vien/sinh-vien-chuong-trinh';

export const createSinhVienChuongTrinh = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSinhVienChuongTrinh = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSinhVienChuongTrinh = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSinhVienChuongTrinh,
  updateSinhVienChuongTrinh,
  deleteSinhVienChuongTrinh,
};
