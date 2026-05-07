import apiClient from '../../../../api/apiConfig.js';

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
