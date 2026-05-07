import apiClient from '../../../../api/apiConfig.js';

const ENDPOINT = '/api/diem/diem-chi-tiet';

export const getAllDiemChiTiet = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDiemChiTietById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDiemChiTiet,
  getDiemChiTietById,
};
