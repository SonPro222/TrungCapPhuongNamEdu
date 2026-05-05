import apiClient from '../../apiConfig';

const ENDPOINT = '/api/chuongTrinh/dieu-kien-tot-nghiep';

export const getAllDieuKienTotNghiep = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDieuKienTotNghiepById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDieuKienTotNghiep,
  getDieuKienTotNghiepById,
};
