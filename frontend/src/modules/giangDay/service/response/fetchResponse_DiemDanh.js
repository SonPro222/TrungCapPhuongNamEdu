import apiClient from '../../apiConfig';

const ENDPOINT = '/api/giang-day/diem-danh';

export const getAllDiemDanh = (params = {}) => {
  return apiClient.get(ENDPOINT, { params });
};

export const getDiemDanhById = (id) => {
  return apiClient.get(`${ENDPOINT}/${id}`);
};

export default {
  getAllDiemDanh,
  getDiemDanhById,
};
