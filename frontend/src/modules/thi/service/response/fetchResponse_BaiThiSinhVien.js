import apiClient from '../../apiConfig';

const ENDPOINT = '/api/thi/bai-thi-sinh-vien';

export const getAllBaiThiSinhVien = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getBaiThiSinhVienById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllBaiThiSinhVien,
  getBaiThiSinhVienById,
};
