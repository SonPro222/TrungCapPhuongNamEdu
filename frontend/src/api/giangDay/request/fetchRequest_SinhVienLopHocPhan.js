import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/sinh-vien-lop-hoc-phan';

export const createSinhVienLopHocPhan = (data) => {
  return apiClient.post(ENDPOINT, data);
};

export const updateSinhVienLopHocPhan = (id, data) => {
  return apiClient.put(`${ENDPOINT}/${id}`, data);
};

export const deleteSinhVienLopHocPhan = (id) => {
  return apiClient.delete(`${ENDPOINT}/${id}`);
};

export default {
  createSinhVienLopHocPhan,
  updateSinhVienLopHocPhan,
  deleteSinhVienLopHocPhan,
};
