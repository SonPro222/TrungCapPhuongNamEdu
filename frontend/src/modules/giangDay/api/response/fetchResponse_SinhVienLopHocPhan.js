import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/giang-day/sinh-vien-lop-hoc-phan';

export const getAllSinhVienLopHocPhan = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSinhVienLopHocPhanById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSinhVienLopHocPhan,
  getSinhVienLopHocPhanById,
};
