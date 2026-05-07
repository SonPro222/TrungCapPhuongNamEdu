import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/diem/bai-nop-sinh-vien';

export const getAllBaiNopSinhVien = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiNopSinhVienById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiNopSinhVien,
  getBaiNopSinhVienById,
};
