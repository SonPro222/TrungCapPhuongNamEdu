import apiClient from '../../apiConfig';

const ENDPOINT = '/api/sinh-vien/sinh-vien-chuong-trinh';

export const getAllSinhVienChuongTrinh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getSinhVienChuongTrinhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllSinhVienChuongTrinh,
  getSinhVienChuongTrinhById,
};
