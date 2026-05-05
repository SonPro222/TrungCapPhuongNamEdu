import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/giao-vien';

export const getAllGiaoVien = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getGiaoVienById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllGiaoVien,
  getGiaoVienById,
};
