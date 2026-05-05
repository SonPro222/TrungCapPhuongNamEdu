import apiClient from '../../apiConfig';

const ENDPOINT = '/api/dao-tao/loai-chuong-trinh';

export const getAllLoaiChuongTrinh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getLoaiChuongTrinhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllLoaiChuongTrinh,
  getLoaiChuongTrinhById,
};
